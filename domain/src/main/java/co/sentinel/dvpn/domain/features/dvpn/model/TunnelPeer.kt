package co.sentinel.dvpn.domain.features.dvpn.model

data class TunnelPeer(
    val allowedIps: String,
    val endpoint: String?,
    val persistentKeepAlive: String?,
    val preSharedKey: String?,
    val publicKey: String
)