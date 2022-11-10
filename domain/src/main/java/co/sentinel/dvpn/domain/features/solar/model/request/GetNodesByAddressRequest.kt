package co.sentinel.dvpn.domain.features.solar.model.request

import com.google.gson.annotations.SerializedName

data class GetNodesByAddressRequest(
    @SerializedName("blockchain_addresses")
    val blockchainAddresses: List<String>,
    @SerializedName("page")
    val page: Int? = null
)