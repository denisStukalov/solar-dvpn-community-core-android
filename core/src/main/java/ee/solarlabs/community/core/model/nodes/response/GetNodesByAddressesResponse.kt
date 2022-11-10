package ee.solarlabs.community.core.model.nodes.response

import com.google.gson.annotations.SerializedName
import java.math.BigInteger

data class GetNodesByAddressesResponse(
    @SerializedName("current_page")
    val currentPage: Int,
    val data: List<Node>,
    var total: Int
) {
    data class Node(
        val id: Long,
        @SerializedName("blockchain_address")
        val blockchainAddress: String,
        @SerializedName("is_trusted")
        val isTrusted: Boolean,
        val moniker: String?,
        @SerializedName("remote_url")
        val remoteUrl: String,
        val status: String,
        @SerializedName("default_price")
        val defaultPrice: BigInteger,
        @SerializedName("bandwidth_upload")
        val bandwidthUpload: Int,
        @SerializedName("bandwidth_download")
        val bandwidthDownload: Int,
        @SerializedName("location_country_code")
        val locationCountryCode: String,
        @SerializedName("location_continent_code")
        val locationContinentCode: String
    )
}
