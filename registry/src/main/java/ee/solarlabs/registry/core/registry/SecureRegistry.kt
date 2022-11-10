package ee.solarlabs.registry.core.registry

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

class SecureRegistry(
    applicationContext: Context
) {

    companion object {
        const val preferencesName = "community_secure_preferences"

    }

    // Create or retrieve the Master Key for encryption/decryption
    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    // Initialize/open an instance of EncryptedSharedPreferences
    private val sharedPreferences = EncryptedSharedPreferences.create(
        preferencesName,
        masterKeyAlias,
        applicationContext,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

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