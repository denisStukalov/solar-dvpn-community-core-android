package co.sentinel.dvpn.domain.features.dvpn.model

data class TunnelConfig(
    val tunnelInterface: TunnelInterface,
    val peers: List<TunnelPeer> = ArrayList()
)
