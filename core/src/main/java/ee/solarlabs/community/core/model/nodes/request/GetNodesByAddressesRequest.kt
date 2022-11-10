package ee.solarlabs.community.core.model.nodes.request

import com.google.gson.annotations.SerializedName

data class GetNodesByAddressesRequest(
    @SerializedName("blockchain_addresses")
    val blockchainAddresses: List<String>,
    @SerializedName("page")
    val page: Int? = null
)