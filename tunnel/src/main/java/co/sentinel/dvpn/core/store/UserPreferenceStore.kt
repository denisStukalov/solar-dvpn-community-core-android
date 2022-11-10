package co.sentinel.dvpn.core.store

import androidx.datastore.preferences.core.Preferences
import co.sentinel.dvpn.domain.features.dvpn.model.DnsServer
import kotlinx.coroutines.flow.Flow

interface UserPreferenceStore {

    val disableKernelModule: Flow<Boolean>
    suspend fun setDisableKernelModule(disable: Boolean?): Preferences

    val multipleTunnels: Flow<Boolean>

    val allowRemoteControlIntents: Flow<Boolean>

    val restoreOnBoot: Flow<Boolean>

    val lastUsedTunnel: Flow<String?>
    suspend fun setLastUsedTunnel(lastUsedTunnel: String?): Preferences

    val runningTunnels: Flow<Set<String>>
    suspend fun setRunningTunnels(runningTunnels: Set<String>?): Preferences

    val subscriptionId: Flow<Long?>
    suspend fun setSubscriptionId(subscriptionId: Long?): Preferences

    val nodeAddress: Flow<String?>
    suspend fun setNodeAddress(nodeAddress: String?): Preferences

    val dns: Flow<DnsServer?>
    suspend fun setDns(dns: DnsServer): Preferences
}