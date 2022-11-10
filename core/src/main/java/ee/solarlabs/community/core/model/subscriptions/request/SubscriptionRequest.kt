package ee.solarlabs.community.core.model.subscriptions.request

import com.google.gson.annotations.SerializedName

data class SubscriptionRequest(
    @SerializedName("node_address")
    val nodeAddress: String,
    val amount: String,
    val currency: String
)