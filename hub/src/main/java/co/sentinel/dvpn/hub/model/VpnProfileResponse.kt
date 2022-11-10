package co.sentinel.dvpn.hub.model

data class VpnProfileResponse(
    val success: Boolean,
    val result: String?,
    val error: Error?
) {

    data class Error(
        val code: Int,
        val message: String
    )
}