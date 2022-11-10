package ee.solarlabs.community.core.mapper.connection

import co.sentinel.dvpn.domain.core.functional.Mapper
import co.sentinel.dvpn.domain.features.dvpn.tasks.connection.GetConnection
import ee.solarlabs.community.core.model.connection.response.GetConnectionResponse

object GetConnectionResponseMapper : Mapper<GetConnection.Success, GetConnectionResponse> {
    override fun map(obj: GetConnection.Success): GetConnectionResponse {
        return GetConnectionResponse(
            nodeAddress = obj.nodeAddress,
            tunnelStatus = when (obj.isConnected) {
                true -> GetConnectionResponse.TunnelStatus.connected
                false -> GetConnectionResponse.TunnelStatus.disconnected
            }
        )
    }
}