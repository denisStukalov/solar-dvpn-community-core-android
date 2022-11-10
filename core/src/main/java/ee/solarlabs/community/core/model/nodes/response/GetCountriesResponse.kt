package ee.solarlabs.community.core.model.nodes.response

import com.google.gson.annotations.SerializedName

data class GetCountriesResponse(
    val code: String, // "ae"
    @SerializedName("nodes_count")
    val nodesCount: Int // 0
)