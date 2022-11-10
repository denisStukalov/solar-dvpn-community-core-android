package ee.solarlabs.community.core.model.connection.request

import com.google.gson.annotations.SerializedName

data class PostConnectionRequest(
    @SerializedName("node_address")
    val nodeAddress: String
)
