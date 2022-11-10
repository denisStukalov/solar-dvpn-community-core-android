package co.sentinel.solar.core.mapper

import co.sentinel.dvpn.domain.core.functional.Mapper
import co.sentinel.dvpn.domain.features.solar.model.Node
import co.sentinel.solar.core.model.GetNodesResponse

object NodeMapper : Mapper<GetNodesResponse.Node, Node?> {
    override fun map(obj: GetNodesResponse.Node): Node? {
        return try {
            Node(
                id = obj.id,
                blockchainAddress = obj.blockchainAddress,
                isTrusted = obj.isTrusted,
                moniker = obj.moniker,
                remoteUrl = obj.remoteUrl,
                status = obj.status,
                defaultPrice = obj.defaultPrice,
                bandwidthUpload = obj.bandwidthUpload,
                bandwidthDownload = obj.bandwidthDownload,
                locationCountryCode = obj.locationCountryCode,
                locationContinentCode = obj.locationContinentCode
            )
        } catch (exception: Exception) {
            null
        }
    }
}