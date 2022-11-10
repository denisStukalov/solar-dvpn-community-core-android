package co.sentinel.cosmos.core.store

import android.content.Context
import android.content.SharedPreferences
import co.sentinel.cosmos.base.BaseCosmosApp


class PrefsStore(app: BaseCosmosApp) {
    companion object {
        const val PREFS_NAME = "wallet_prefs"

        private const val KEY_PASSCODE = "KEY_PASSCODE"
    }

    private val sharedPreferences: SharedPreferences =
        app.context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun storePasscode(passcode: String) {
        sharedPreferences.edit().putString(KEY_PASSCODE, passcode).apply()
    }

    fun retrievePasscode(): String {
        return sharedPreferences.getString(KEY_PASSCODE, "")!!
    }

}