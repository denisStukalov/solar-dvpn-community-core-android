package co.sentinel.dvpn.domain.features.hub.model

data class VpnProfile(
    val address: String,
    val host: String,
    val listenPort: String,
    val peerEndpoint: String,
    val peerPubKeyBytes: ByteArray
)