package co.sentinel.dvpn.domain.features.dvpn

import co.sentinel.dvpn.domain.core.exception.Failure
import co.sentinel.dvpn.domain.core.functional.Either
import co.sentinel.dvpn.domain.core.interactor.BaseUseCase
import co.sentinel.dvpn.domain.features.dvpn.model.DvpnTunnel
import co.sentinel.dvpn.domain.features.dvpn.source.DVPNRepository

class SetTunnelState(
    private val repository: DVPNRepository
) : BaseUseCase<SetTunnelState.Success, SetTunnelState.SetTunnelStateParams>() {

    override suspend fun run(params: SetTunnelStateParams): Either<Failure, Success> {
        return repository.setTunnelState(params)
    }

    data class Success(val tunnel: DvpnTunnel)

    data class SetTunnelStateParams(val tunnelName: String, val tunnelState: DvpnTunnel.State)

    sealed class SetTunnelStateFailure : Failure.FeatureFailure() {
        object TunnelNotFound : SetTunnelStateFailure()
        object SetStateFailed : SetTunnelStateFailure()
    }
}