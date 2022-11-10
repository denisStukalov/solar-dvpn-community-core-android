package ee.solarlabs.community.core.mapper.subscriptions

import co.sentinel.dvpn.domain.core.functional.Mapper
import co.sentinel.dvpn.domain.features.hub.model.Quota
import ee.solarlabs.community.core.model.subscriptions.response.QuotaResponse

object QuotaResponseMapper : Mapper<Quota, QuotaResponse> {
    override fun map(obj: Quota): QuotaResponse {
        return QuotaResponse(
            address = obj.address,
            consumed = obj.consumed,
            allocated = obj.allocated
        )
    }
}