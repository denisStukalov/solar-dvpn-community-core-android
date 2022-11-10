package co.sentinel.dvpn.core.mapper

import co.sentinel.dvpn.core.model.TunnelWrapper
import co.sentinel.dvpn.domain.core.functional.Mapper
import co.sentinel.dvpn.domain.features.dvpn.model.DvpnTunnel

object DomainTunnelToTunnelMapper : Mapper<DvpnTunnel, TunnelWrapper> {
    override fun map(obj: DvpnTunnel) = TunnelWrapper(
        name = obj.name,
        state = DomainStateToStateMapper.map(obj.state),
        config = obj.config?.let { DomainConfigToConfigMapper.map(it) },
        duration = obj.duration
    )
}

object TunnelToDomainTunnelMapper : Mapper<TunnelWrapper, DvpnTunnel> {
    override fun map(obj: TunnelWrapper) = DvpnTunnel(
        name = obj.name,
        state = StateToDomainStateMapper.map(obj.state),
        config = obj.config?.let { ConfigToDomainConfigMapper.map(it) },
        duration = obj.duration
    )
}
