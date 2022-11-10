package co.sentinel.cosmos.task.gRpcTask

import co.sentinel.cosmos.base.BaseChain
import co.sentinel.cosmos.base.BaseConstant
import co.sentinel.cosmos.base.BaseCosmosApp
import co.sentinel.cosmos.dao.Account
import co.sentinel.cosmos.network.ChannelBuilder
import co.sentinel.cosmos.task.CommonTask
import co.sentinel.cosmos.task.TaskResult
import cosmos.distribution.v1beta1.Distribution.DelegationDelegatorReward
import cosmos.distribution.v1beta1.QueryGrpc
import cosmos.distribution.v1beta1.QueryOuterClass.QueryDelegationTotalRewardsRequest
import kotlinx.coroutines.guava.await
import timber.log.Timber
import java.util.concurrent.TimeUnit


class AllRewardGrpcTask(
    app: BaseCosmosApp,
    private val mChain: BaseChain,
    private val mAccount: Account
) : CommonTask(app) {
    private val mResultData = ArrayList<DelegationDelegatorReward>()
    private val mStub: QueryGrpc.QueryFutureStub

    init {
        mResult.taskType = BaseConstant.TASK_GRPC_FETCH_ALL_REWARDS
        mStub = QueryGrpc.newFutureStub(ChannelBuilder.getChain(mChain))
            .withDeadlineAfter(ChannelBuilder.TIME_OUT.toLong(), TimeUnit.SECONDS)
    }

    override suspend fun doInBackground(vararg strings: String): TaskResult {
        try {
            val request = QueryDelegationTotalRewardsRequest.newBuilder()
                .setDelegatorAddress(mAccount.address).build()
            val response = mStub.delegationTotalRewards(request).await()
            mResultData.addAll(response.rewardsList)
            mResult.isSuccess = true
            mResult.resultData = mResultData
            //            Timber.w("AllReward " + mResultData.size());
        } catch (e: Exception) {
            Timber.e("AllRewardGrpcTask " + e.message)
            mResult.errorMsg = e.localizedMessage ?: "Task error occurred."
            mResult.isSuccess = false
        }
        return mResult
    }
}