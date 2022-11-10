package ee.solarlabs.community.core.model.nodes.response

import com.google.gson.annotations.SerializedName

data class GetContinentsResponse(
    val code: String, // "AF"
    @SerializedName("nodes_count")
    val nodesCount: Int // 0
)