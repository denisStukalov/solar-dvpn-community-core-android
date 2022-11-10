package ee.solarlabs.community.core.mapper.nodes

import co.sentinel.dvpn.domain.core.functional.Mapper
import co.sentinel.dvpn.domain.features.solar.tasks.GetNodesByAddresses
import ee.solarlabs.community.core.model.nodes.response.GetNodesByAddressesResponse

object GetNodesByAddressesResponseMapper :
    Mapper<GetNodesByAddresses.Success, GetNodesByAddressesResponse> {
    override fun map(obj: GetNodesByAddresses.Success): GetNodesByAddressesResponse {
        return GetNodesByAddressesResponse(
            currentPage = obj.currentPage,
            total = obj.total,
            data = obj.data.map {
                GetNodesByAddressesResponse.Node(
                    id = it.id,
                    blockchainAddress = it.blockchainAddress,
                    isTrusted = it.isTrusted,
                    moniker = it.moniker,
                    remoteUrl = it.remoteUrl,
                    status = it.status,
                    defaultPrice = it.defaultPrice,
                    bandwidthDownload = it.bandwidthDownload,
                    bandwidthUpload = it.bandwidthUpload,
                    locationCountryCode = it.locationCountryCode,
                    locationContinentCode = it.locationContinentCode
                )
            }
        )
    }
}