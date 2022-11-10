package ee.solarlabs.community.core.model.connection.response

data class GetConnectionResponse(
    val nodeAddress: String,
    val tunnelStatus: TunnelStatus
) {
    enum class TunnelStatus {
        connected, disconnected
    }
}
