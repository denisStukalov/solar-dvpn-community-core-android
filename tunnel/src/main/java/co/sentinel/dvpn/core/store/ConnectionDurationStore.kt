package co.sentinel.dvpn.core.store

import android.content.Context
import android.content.SharedPreferences
import co.sentinel.dvpn.domain.core.extension.empty

interface ConnectionDurationStore {
    fun saveConnectionTimestamp(subscriptionId: Long, timestamp: Long)
    fun getConnectionTimestamp(subscriptionId: Long?): Long?
    fun clearConnectionTimestamp()
    fun clear()
}

class ConnectionDurationStoreImpl(context: Context) : ConnectionDurationStore {

    companion object {
        private const val VPN_CONNECTION_TIMESTAMP_PREFS = "vpn_connection_duration_prefs"
        private const val VPN_CONNECTION_TIMESTAMP = "vpn_connection_duration"
    }

    private val prefs: SharedPreferences = context.getSharedPreferences(
        VPN_CONNECTION_TIMESTAMP_PREFS,
        Context.MODE_PRIVATE
    )

    override fun saveConnectionTimestamp(subscriptionId: Long, timestamp: Long) {
        prefs.edit().putString(
            VPN_CONNECTION_TIMESTAMP,
            "$subscriptionId,$timestamp"
        ).apply()
    }

    override fun getConnectionTimestamp(subscriptionId: Long?): Long? {
        return subscriptionId?.let {
            val prefString = prefs.getString(VPN_CONNECTION_TIMESTAMP, String.empty())!!
            return if (prefString.startsWith(subscriptionId.toString(), false)) {
                prefString.substringAfter(",")?.toLong()
            } else null
        }
    }

    override fun clearConnectionTimestamp() {
        prefs.edit().putString(
            VPN_CONNECTION_TIMESTAMP,
            String.empty()
        ).apply()
    }

    override fun clear() {
        prefs.edit().clear().apply()
    }

    private fun String.substringAfter(delimiter: String): String? {
        return indexOf(delimiter).let { if (it == -1) null else substring(it + 1) }
    }
}

