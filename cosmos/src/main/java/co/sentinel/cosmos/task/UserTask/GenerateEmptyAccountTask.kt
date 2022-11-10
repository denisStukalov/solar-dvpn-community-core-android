package co.sentinel.cosmos.task.UserTask

import co.sentinel.cosmos.base.BaseConstant
import co.sentinel.cosmos.base.BaseCosmosApp
import co.sentinel.cosmos.dao.Account
import co.sentinel.cosmos.task.CommonTask
import co.sentinel.cosmos.task.TaskResult


class GenerateEmptyAccountTask(app: BaseCosmosApp) : CommonTask(app) {
    init {
        mResult.taskType = BaseConstant.TASK_INIT_EMPTY_ACCOUNT
    }

    /**
     *
     * @param strings
     * strings[0] : chainType
     * strings[1] : address
     * @return
     */
    override suspend fun doInBackground(vararg strings: String): TaskResult {
        val id: Long = app.baseDao.onInsertAccount(
            onGenEmptyAccount(
                strings[0],
                strings[1]
            )
        )
        if (id > 0) {
            mResult.isSuccess = true
            app.baseDao.setLastUser(id)
        } else {
            mResult.errorMsg = "Already existed account"
            mResult.errorCode = 7001
        }
        return mResult
    }

    private fun onGenEmptyAccount(chainType: String?, address: String?): Account {
        val newAccount = Account.getNewInstance()
        newAccount.address = address
        newAccount.baseChain = chainType
        newAccount.hasPrivateKey = false
        newAccount.fromMnemonic = false
        newAccount.importTime = System.currentTimeMillis()
        return newAccount
    }
}
