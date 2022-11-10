package co.sentinel.cosmos.core.store

import android.content.Context
import android.content.SharedPreferences
import co.sentinel.cosmos.base.BaseCosmosApp
import co.sentinel.cosmos.model.type.AccountBalance
import com.google.gson.Gson

interface BalanceStore {
    fun storeBalance(accountBalance: AccountBalance)
    fun getBalance(): AccountBalance?
    fun clear()
}

class BalanceStoreImpl(app: BaseCosmosApp) : BalanceStore {
    companion object {
        const val PREFS_NAME = "balance_prefs"

        private const val KEY_BALANCE = "KEY_BALANCE"
    }

    private val sharedPreferences: SharedPreferences =
        app.context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override fun storeBalance(accountBalance: AccountBalance) {
        sharedPreferences.edit().putString(
            KEY_BALANCE,
            Gson().toJson(accountBalance)
        ).apply()
    }

    override fun getBalance(): AccountBalance? {
        val balance = sharedPreferences.getString(KEY_BALANCE, "")
        return Gson().fromJson(balance, AccountBalance::class.java)
    }

    override fun clear() {
        sharedPreferences.edit().clear().apply()
    }


}