package co.sentinel.cosmos.task.UserTask

import co.sentinel.cosmos.R
import co.sentinel.cosmos.base.BaseConstant
import co.sentinel.cosmos.base.BaseCosmosApp
import co.sentinel.cosmos.crypto.CryptoHelper
import co.sentinel.cosmos.dao.Account
import co.sentinel.cosmos.task.CommonTask
import co.sentinel.cosmos.task.TaskResult


class CheckMnemonicTask(app: BaseCosmosApp, account: Account) : CommonTask(app) {
    private val mAccount: Account

    init {
        mResult.taskType = BaseConstant.TASK_CHECK_MNEMONIC
        mAccount = account
    }

    /**
     * @param strings strings[0] : password
     * @return
     */
    override suspend fun doInBackground(vararg strings: String): TaskResult {
        val entropy = CryptoHelper.doDecryptData(
            app.context.getString(R.string.key_mnemonic) + mAccount.uuid,
            mAccount.resource,
            mAccount.spec
        )
        mResult.resultData = entropy
        mResult.isSuccess = true
        return mResult
    }
}
