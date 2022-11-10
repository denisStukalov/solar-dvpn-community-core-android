package ee.solarlabs.community.core.model.dns.request

data class DnsListResponse(
    val servers: List<Dns>
) {
    data class Dns(
        val name: String, // "handshake"
        val addresses: String // "103.196.38.38, 103.196.38.39"
    )
}
