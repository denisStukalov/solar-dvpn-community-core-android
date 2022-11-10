package co.sentinel.cosmos.task.gRpcTask

import co.sentinel.cosmos.base.BaseChain
import co.sentinel.cosmos.base.BaseConstant
import co.sentinel.cosmos.base.BaseCosmosApp
import co.sentinel.cosmos.dao.Account
import co.sentinel.cosmos.network.ChannelBuilder
import co.sentinel.cosmos.task.CommonTask
import co.sentinel.cosmos.task.TaskResult
import com.google.protobuf.ByteString
import cosmos.bank.v1beta1.QueryOuterClass.QueryAllBalancesResponse
import cosmos.base.query.v1beta1.Pagination.PageRequest
import cosmos.staking.v1beta1.QueryGrpc
import cosmos.staking.v1beta1.QueryOuterClass.QueryDelegatorDelegationsRequest
import cosmos.staking.v1beta1.Staking.DelegationResponse
import kotlinx.coroutines.guava.await
import timber.log.Timber
import java.util.concurrent.TimeUnit


class DelegationsGrpcTask(
    app: BaseCosmosApp,
    private val mChain: BaseChain,
    private val mAccount: Account
) : CommonTask(app) {
    private val mResultData = ArrayList<DelegationResponse>()
    private val mStub: QueryGrpc.QueryFutureStub

    init {
        mResult.taskType = BaseConstant.TASK_GRPC_FETCH_DELEGATIONS
        mStub = QueryGrpc.newFutureStub(ChannelBuilder.getChain(mChain))
            .withDeadlineAfter(ChannelBuilder.TIME_OUT.toLong(), TimeUnit.SECONDS)
    }

    override suspend fun doInBackground(vararg strings: String): TaskResult {
        try {
            val request =
                QueryDelegatorDelegationsRequest.newBuilder().setDelegatorAddr(mAccount.address)
                    .build()
            val response = mStub.delegatorDelegations(request).await()
            mResultData.addAll(response.delegationResponsesList)
            mResult.isSuccess = true
            mResult.resultData = mResultData
        } catch (e: Exception) {
            Timber.e("DelegationsGrpc " + e.message)
            mResult.errorMsg = e.localizedMessage ?: "Task error occurred."
            mResult.isSuccess = false
        }
        return mResult
    }

    private suspend fun pageJob(nextKey: ByteString): QueryAllBalancesResponse? {
        try {
            val pageRequest = PageRequest.newBuilder().setKey(nextKey).build()
            val request = QueryDelegatorDelegationsRequest.newBuilder().setPagination(pageRequest)
                .setDelegatorAddr(mAccount.address).build()
            val response = mStub.delegatorDelegations(request).await()
            mResultData.addAll(response.delegationResponsesList)
            if (response.hasPagination() && response.pagination.nextKey.size() > 0) {
                pageJob(response.pagination.nextKey)
            }
        } catch (e: Exception) {
            Timber.e("DelegationsGrpc pageJob " + e.message)
        }
        return null
    }
}
