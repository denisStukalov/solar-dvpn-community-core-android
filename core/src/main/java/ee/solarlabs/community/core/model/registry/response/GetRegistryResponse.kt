package ee.solarlabs.community.core.model.registry.response

import com.google.gson.annotations.SerializedName

data class GetRegistryResponse(
    val key: String,
    val value: String,
    @SerializedName("is_secure")
    val isSecure: Boolean
)
