package ee.solarlabs.community.core.model.registry.request

import com.google.gson.annotations.SerializedName

data class PostRegistryRequest(
    val key: String,
    val value: String,
    @SerializedName("is_secure")
    val isSecure: Boolean
)
