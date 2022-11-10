package co.sentinel.dvpn.cache.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import co.sentinel.dvpn.cache.data.LastSessionDatastore.PreferencesKeys.LAST_SESSION
import co.sentinel.dvpn.cache.data.LastSessionDatastore.PreferencesKeys.dataStore
import co.sentinel.dvpn.domain.features.hub.model.Session
import com.google.gson.Gson
import kotlinx.coroutines.flow.first

class LastSessionDatastore(context: Context) {
    private val dataStore = context.dataStore

    suspend fun getLastSession() =
        dataStore.data.first().toPreferences()[LAST_SESSION]?.let {
            Gson().fromJson(it, Session::class.java)
        }

    suspend fun setLastSession(session: Session) {
        dataStore.edit { preferences ->
            preferences[LAST_SESSION] = Gson().toJson(session)
        }
    }

    private object PreferencesKeys {
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "last_session")

        val LAST_SESSION = stringPreferencesKey("session")
    }
}