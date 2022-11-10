package ee.solarlabs.community.core.model.purchases.request

import com.google.gson.annotations.SerializedName

data class PurchaseRequest(
    @SerializedName("package_identifier")
    val packageIdentifier: String // "dvpn_100"
)