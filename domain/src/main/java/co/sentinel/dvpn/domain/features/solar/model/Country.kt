package co.sentinel.dvpn.domain.features.solar.model

import com.google.gson.annotations.SerializedName

data class Country(
    val code: String,
    @SerializedName("nodes_count")
    val nodesCount: Int
)