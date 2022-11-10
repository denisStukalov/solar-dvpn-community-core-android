package ee.solarlabs.community.core.mapper.registry

import co.sentinel.dvpn.domain.core.functional.Mapper
import co.sentinel.dvpn.domain.features.registry.model.Registry
import ee.solarlabs.community.core.model.registry.response.GetRegistryResponse

object GetRegistryMapper : Mapper<Registry, GetRegistryResponse> {
    override fun map(obj: Registry): GetRegistryResponse {
        return GetRegistryResponse(
            key = obj.key,
            value = obj.value,
            isSecure = obj.isSecure
        )
    }
}