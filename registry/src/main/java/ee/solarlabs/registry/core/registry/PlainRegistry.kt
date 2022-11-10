package ee.solarlabs.registry.core.registry

import android.content.Context
import android.content.SharedPreferences

class PlainRegistry(
    applicationContext: Context
) {

    companion object {
        const val preferencesName = "community_regular_preferences"
    }

    private val sharedPreferences: SharedPreferences =
        applicationContext.getSharedPreferences(preferencesName, Context.MODE_PRIVATE)

    fun storeKeyValue(key: String, value: String) =
        with(sharedPreferences.edit()) {
            putString(key, value)
            apply()
        }

    fun retrieveKeyValue(key: String): String? =
        with(sharedPreferences) {
            getString(key, null)
        }

    fun deleteKeyValue(key: String) =
        with(sharedPreferences.edit()) {
            remove(key)
            apply()
        }
}