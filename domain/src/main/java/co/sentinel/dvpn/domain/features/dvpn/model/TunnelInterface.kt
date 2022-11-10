package co.sentinel.dvpn.domain.features.dvpn.model

data class TunnelInterface(
    val excludedApplications: List<String>,
    val includedApplications: List<String>,
    val addresses: String,
    val dnsServers: String,
    val listenPort: String,
    val mtu: String,
    val privateKey: String?,
    val publicKey: String?
)