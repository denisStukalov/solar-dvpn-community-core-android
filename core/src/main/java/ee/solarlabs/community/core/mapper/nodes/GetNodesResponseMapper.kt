package ee.solarlabs.community.core.mapper.nodes

import co.sentinel.dvpn.domain.core.functional.Mapper
import co.sentinel.dvpn.domain.features.solar.tasks.GetNodes
import ee.solarlabs.community.core.model.nodes.response.GetNodesResponse

object GetNodesResponseMapper : Mapper<GetNodes.Success, GetNodesResponse> {
    override fun map(obj: GetNodes.Success): GetNodesResponse {
        return GetNodesResponse(
            currentPage = obj.currentPage,
            total = obj.total,
            data = obj.data.map {
                GetNodesResponse.Node(
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