package co.sentinel.dvpn.domain.features.dvpn

import co.sentinel.dvpn.domain.core.exception.Failure
import co.sentinel.dvpn.domain.core.functional.Either
import co.sentinel.dvpn.domain.core.interactor.BaseUseCase
import co.sentinel.dvpn.domain.features.dvpn.model.DvpnTunnel
import co.sentinel.dvpn.domain.features.dvpn.source.DVPNRepository

class GetTunnel(
    private val repository: DVPNRepository
) : BaseUseCase<GetTunnel.Success, GetTunnel.Params>() {

    override suspend fun run(params: Params): Either<Failure, Success> {
        return repository.getTunnel(params.tunnelName)
    }

    data class Success(val tunnel: DvpnTunnel)
    data class Params(val tunnelName: String)


    sealed class GetTunnelFailure : Failure.FeatureFailure() {
        object TunnelNotFound : GetTunnelFailure()
    }
}