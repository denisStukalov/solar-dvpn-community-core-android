package co.sentinel.dvpn.core.mapper

import co.sentinel.dvpn.domain.core.functional.Mapper
import com.wireguard.android.backend.Tunnel
import co.sentinel.dvpn.domain.features.dvpn.model.DvpnTunnel.State as DomainState

object StateToDomainStateMapper : Mapper<Tunnel.State, DomainState> {
    override fun map(obj: Tunnel.State) = when (obj) {
        Tunnel.State.DOWN -> DomainState.DOWN
        Tunnel.State.TOGGLE -> DomainState.TOGGLE
        Tunnel.State.UP -> DomainState.UP
    }
}

object DomainStateToStateMapper : Mapper<DomainState, Tunnel.State> {
    override fun map(obj: DomainState) = when (obj) {
        DomainState.DOWN -> Tunnel.State.DOWN
        DomainState.TOGGLE -> Tunnel.State.TOGGLE
        DomainState.UP -> Tunnel.State.UP
    }
}
