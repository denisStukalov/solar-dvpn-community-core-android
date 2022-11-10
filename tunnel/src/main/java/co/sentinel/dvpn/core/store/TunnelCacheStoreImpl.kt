package co.sentinel.dvpn.core.store

import co.sentinel.dvpn.core.model.TunnelComparator
import co.sentinel.dvpn.core.model.TunnelWrapper
import co.sentinel.dvpn.domain.core.functional.SortedKeyedArrayList

class TunnelCacheStoreImpl : TunnelCacheStore {
    private val tunnelMap = SortedKeyedArrayList<String, TunnelWrapper>(TunnelComparator)
    private var lastUsedTunnel: TunnelWrapper? = null

    override fun add(tunnel: TunnelWrapper) {
        if (!tunnelMap.containsKey(tunnel.key)) {
            tunnelMap.add(tunnel)
        }
    }

    override fun populate(tunnelList: List<TunnelWrapper>) {
        tunnelMap.clear()
        tunnelMap.addAll(tunnelList)
    }

    override fun delete(tunnel: TunnelWrapper) {
        tunnelMap.remove(tunnel)
    }

    override fun getTunnelList() = tunnelMap

    override fun updateLastUsedTunnel(tunnel: TunnelWrapper?) {
        lastUsedTunnel = tunnel
    }

    override fun getLastUsedTunnel() = lastUsedTunnel
}