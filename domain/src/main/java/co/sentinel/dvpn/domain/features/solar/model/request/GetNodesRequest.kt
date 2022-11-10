package co.sentinel.dvpn.domain.features.solar.model.request

import co.sentinel.dvpn.domain.features.solar.model.OrderBy
import co.sentinel.dvpn.domain.features.solar.model.Status

data class GetNodesRequest(
    val continent: String? = null,
    val country: String? = null,
    val status: Status? = Status.STATUS_ACTIVE,
    val minPrice: Int? = null,
    val maxPrice: Int? = null,
    val orderBy: OrderBy? = null,
    val query: String? = null,
    var page: Int? = null
)