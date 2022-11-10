package ee.solarlabs.community.core.mapper.dns

import co.sentinel.dvpn.domain.core.functional.Mapper
import co.sentinel.dvpn.domain.features.dvpn.model.DnsServer
import ee.solarlabs.community.core.model.dns.request.DnsResponse

object DnsResponseMapper : Mapper<DnsServer, DnsResponse> {
    override fun map(obj: DnsServer): DnsResponse {
        return DnsResponse(obj.name.name, obj.addresses)
    }
}