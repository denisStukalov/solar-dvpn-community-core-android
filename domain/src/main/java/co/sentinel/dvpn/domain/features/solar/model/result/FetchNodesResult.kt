package co.sentinel.dvpn.domain.features.solar.model.result

import co.sentinel.dvpn.domain.features.solar.model.Node

data class FetchNodesResult(
    val currentPage: Int,
    val data: List<Node>,
    val total: Int
)