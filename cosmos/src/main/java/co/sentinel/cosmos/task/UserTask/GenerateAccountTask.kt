package co.sentinel.cosmos.task.UserTask

import co.sentinel.cosmos.R
import co.sentinel.cosmos.base.BaseChain
import co.sentinel.cosmos.base.BaseConstant
import co.sentinel.cosmos.base.BaseCosmosApp
import co.sentinel.cosmos.crypto.CryptoHelper
import co.sentinel.cosmos.dao.Account
import co.sentinel.cosmos.task.CommonTask
import co.sentinel.cosmos.task.TaskResult
import co.sentinel.cosmos.utils.WKey


class GenerateAccountTask(
    app: BaseCosmosApp,
    private val mBaseChain: BaseChain,
    private val mNewPath: Boolean
) : CommonTask(app) {
    init {
        mResult.taskType = BaseConstant.TASK_INIT_ACCOUNT
    }

    /**
     * @param strings strings[0] : path
     * strings[1] : entorpy seed
     * strings[2] : word size
     * @return
     */
    override suspend fun doInBackground(vararg strings: String): TaskResult {
        try {
            val id: Long = app.baseDao.onInsertAccount(
                onGenAccount(
                    strings[1],
                    strings[0], strings[2]
                )
            )
            if (id > 0) {
                mResult.isSuccess = true
                app.baseDao.setLastUser(id)
            } else {
                mResult.errorMsg = "Already existed account"
                mResult.errorCode = 7001
            }
        } catch (e: Exception) {
            mResult.errorMsg = e.localizedMessage ?: "Task error occurred."
            mResult.isSuccess = false
        }
        return mResult
    }

    private fun onGenAccount(entropy: String, path: String, msize: String): Account {
        val newAccount = Account.getNewInstance()
        val dKey = WKey.getKeyWithPathfromEntropy(mBaseChain, entropy, path.toInt(), mNewPath)
        val encR = CryptoHelper.doEncryptData(
            app.context.getString(R.string.key_mnemonic) + newAccount.uuid, entropy, false
        )

        newAccount.address = WKey.getDpAddress(mBaseChain, dKey.publicKeyAsHex)

        newAccount.baseChain = mBaseChain.chain
        newAccount.hasPrivateKey = true
        newAccount.resource = encR.encDataString
        newAccount.spec = encR.ivDataString
        newAccount.fromMnemonic = true
        newAccount.path = path
        newAccount.msize = msize.toInt()
        newAccount.importTime = System.currentTimeMillis()
        newAccount.newBip44 = mNewPath
        return newAccount
    }
}