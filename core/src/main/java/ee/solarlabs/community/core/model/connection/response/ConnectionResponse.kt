package ee.solarlabs.community.core.model.connection.response

import com.google.gson.annotations.JsonAdapter
import ee.solarlabs.community.core.model.Error
import ee.solarlabs.community.core.util.FailureReasonSerializer

class ConnectionResponse(
    val type: String = "tunnelStatus",
    val value: String
) {
    companion object {
        val connected = ConnectionResponse(value = "connected")
        val disconnected = ConnectionResponse(value = "disconnected")
    }
}

class ConnectionErrorResponse(
    val type: String = "error",
    @JsonAdapter(FailureReasonSerializer::class)
    val value: Error.InnerError.FailureReason
)
