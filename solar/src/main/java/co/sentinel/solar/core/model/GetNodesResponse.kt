package co.sentinel.solar.core.model

import com.google.gson.annotations.SerializedName
import java.math.BigInteger

data class GetNodesResponse(
    @SerializedName("current_page")
    val currentPage: Int,
    @SerializedName("data")
    val data: List<Node>,
    @SerializedName("first_page_url")
    val firstPageUrl: String,
    @SerializedName("from")
    val from: Int,
    @SerializedName("last_page")
    val lastPage: Int,
    @SerializedName("last_page_url")
    val lastPageUrl: String,
    @SerializedName("next_page_url")
    val nextPageUrl: String,
    @SerializedName("path")
    val path: String,
    @SerializedName("per_page")
    val perPage: Int,
    @SerializedName("prev_page_url")
    val prevPageUrl: String,
    @SerializedName("to")
    val to: Int,
    @SerializedName("total")
    val total: Int
) {
    data class Node(
        @SerializedName("id")
        val id: Long,
        @SerializedName("blockchain_address")
        val blockchainAddress: String,
        @SerializedName("is_trusted")
        val isTrusted: Boolean,
        @SerializedName("moniker")
        val moniker: String?,
        @SerializedName("remote_url")
        val remoteUrl: String,
        @SerializedName("status")
        val status: String,
        @SerializedName("status_updated_at")
        val statusUpdatedAt: String,
        @SerializedName("default_price")
        val defaultPrice: BigInteger,
        @SerializedName("active_peers")
        val activePeers: Int,
        @SerializedName("qos_max_peers")
        val qosMaxPeers: Int?,
        @SerializedName("bandwidth_upload")
        val bandwidthUpload: Int,
        @SerializedName("bandwidth_download")
        val bandwidthDownload: Int,
        @SerializedName("is_handshake_enabled")
        val isHandshakeEnabled: Boolean,
        @SerializedName("interval_set_sessions")
        val intervalSetSessions: Long?,
        @SerializedName("interval_update_sessions")
        val intervalUpdateSessions: Long?,
        @SerializedName("interval_update_status")
        val intervalUpdateStatus: Long?,
        @SerializedName("location_city")
        val locationCity: String?,
        @SerializedName("location_country_code")
        val locationCountryCode: String,
        @SerializedName("location_continent_code")
        val locationContinentCode: String,
        @SerializedName("location_lat")
        val locationLat: Double,
        @SerializedName("location_lon")
        val locationLon: Double,
        @SerializedName("version")
        val version: String?,
        @SerializedName("data_fetched_at")
        val dataFetchedAt: String?,
        @SerializedName("created_at")
        val createdAt: String?,
        @SerializedName("updated_at")
        val updatedAt: String?,
        @SerializedName("latency")
        val latency: Long = 0L
    )
}