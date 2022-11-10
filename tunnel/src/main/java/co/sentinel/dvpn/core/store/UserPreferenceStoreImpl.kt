package co.sentinel.dvpn.core.store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import co.sentinel.dvpn.domain.features.dvpn.model.DnsServer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class UserPreferenceStoreImpl(private val context: Context) : UserPreferenceStore {
    private val Context.preferencesDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    private val DISABLE_KERNEL_MODULE = booleanPreferencesKey("disable_kernel_module")
    override val disableKernelModule: Flow<Boolean>
        get() = context.preferencesDataStore.data.map {
            it[DISABLE_KERNEL_MODULE] ?: false
        }

    override suspend fun setDisableKernelModule(disable: Boolean?) =
        context.preferencesDataStore.edit { prefs ->
            prefs.apply {
                if (disable == null)
                    remove(DISABLE_KERNEL_MODULE)
                else
                    this[DISABLE_KERNEL_MODULE] = disable
            }
        }

    private val MULTIPLE_TUNNELS = booleanPreferencesKey("multiple_tunnels")
    override val multipleTunnels: Flow<Boolean>
        get() = context.preferencesDataStore.data.map {
            it[MULTIPLE_TUNNELS] ?: false
        }

    private val ALLOW_REMOTE_CONTROL_INTENTS = booleanPreferencesKey("allow_remote_control_intents")
    override val allowRemoteControlIntents: Flow<Boolean>
        get() = context.preferencesDataStore.data.map {
            it[ALLOW_REMOTE_CONTROL_INTENTS] ?: false
        }

    private val RESTORE_ON_BOOT = booleanPreferencesKey("restore_on_boot")
    override val restoreOnBoot: Flow<Boolean>
        get() = context.preferencesDataStore.data.map {
            it[RESTORE_ON_BOOT] ?: false
        }

    private val LAST_USED_TUNNEL = stringPreferencesKey("last_used_tunnel")
    override val lastUsedTunnel: Flow<String?>
        get() = context.preferencesDataStore.data.map {
            it[LAST_USED_TUNNEL]
        }

    override suspend fun setLastUsedTunnel(lastUsedTunnel: String?) =
        context.preferencesDataStore.edit { prefs ->
            prefs.apply {
                if (lastUsedTunnel == null)
                    remove(LAST_USED_TUNNEL)
                else
                    this[LAST_USED_TUNNEL] = lastUsedTunnel
            }
        }

    private val RUNNING_TUNNELS = stringSetPreferencesKey("enabled_configs")
    override val runningTunnels: Flow<Set<String>>
        get() = context.preferencesDataStore.data.map {
            it[RUNNING_TUNNELS] ?: setOf()
        }

    override suspend fun setRunningTunnels(runningTunnels: Set<String>?) =
        context.preferencesDataStore.edit { prefs ->
            prefs.apply {
                if (runningTunnels == null)
                    remove(RUNNING_TUNNELS)
                else
                    this[RUNNING_TUNNELS] = runningTunnels
            }
        }

    private val SUBSCRIPTION_ID = longPreferencesKey("subscription_id")
    override val subscriptionId: Flow<Long?>
        get() = context.preferencesDataStore.data.map {
            it[SUBSCRIPTION_ID]
        }

    override suspend fun setSubscriptionId(subscriptionId: Long?) =
        context.preferencesDataStore.edit { prefs ->
            prefs.apply {
                if (subscriptionId == null)
                    remove(SUBSCRIPTION_ID)
                else
                    this[SUBSCRIPTION_ID] = subscriptionId
            }
        }

    private val NODE_ADDRESS_ID = stringPreferencesKey("node_address")
    override val nodeAddress: Flow<String?>
        get() = context.preferencesDataStore.data.map {
            it[NODE_ADDRESS_ID]
        }

    override suspend fun setNodeAddress(nodeAddress: String?) =
        context.preferencesDataStore.edit { prefs ->
            prefs.apply {
                if (nodeAddress == null)
                    remove(NODE_ADDRESS_ID)
                else
                    this[NODE_ADDRESS_ID] = nodeAddress
            }
        }

    private val DNS = stringPreferencesKey("dns")
    override val dns: Flow<DnsServer?>
        get() = context.preferencesDataStore.data.map { preferences ->
            preferences[DNS]?.let {
                DnsServer.deserializeFromJsonString(it)
            }
        }

    override suspend fun setDns(dns: DnsServer) =
        context.preferencesDataStore.edit { prefs ->
            prefs.apply {
                this[DNS] = dns.serializeToJsonString()
            }
        }
}