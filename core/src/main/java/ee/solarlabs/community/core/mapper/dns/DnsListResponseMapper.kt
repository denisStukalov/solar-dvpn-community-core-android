package ee.solarlabs.community.core.mapper.dns

import co.sentinel.dvpn.domain.core.functional.Mapper
import co.sentinel.dvpn.domain.features.dvpn.model.DnsServer
import ee.solarlabs.community.core.model.dns.request.DnsListResponse

object DnsListResponseMapper : Mapper<List<DnsServer>, DnsListResponse> {
    override fun map(obj: List<DnsServer>): DnsListResponse {
        return DnsListResponse(obj.map {
            DnsListResponse.Dns(it.name.name, it.addresses)
        })
    }
}