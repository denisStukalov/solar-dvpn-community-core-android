package co.sentinel.dvpn.domain.features.dvpn

import co.sentinel.dvpn.domain.core.exception.Failure
import co.sentinel.dvpn.domain.core.functional.Either
import co.sentinel.dvpn.domain.core.interactor.BaseUseCase
import co.sentinel.dvpn.domain.features.dvpn.model.DvpnTunnel
import co.sentinel.dvpn.domain.features.dvpn.model.TunnelConfig
import co.sentinel.dvpn.domain.features.dvpn.source.DVPNRepository

class CreateTunnel(private val repository: DVPNRepository) :
    BaseUseCase<CreateTunnel.Success, CreateTunnel.CreateTunnelParams>() {

    override suspend fun run(params: CreateTunnelParams): Either<Failure, Success> {
        return repository.create(params)
    }

    data class CreateTunnelParams(val name: String, val tunnelConfig: TunnelConfig)

    data class Success(val tunnel: DvpnTunnel)

    sealed class CreateTunnelFailure : Failure.FeatureFailure() {
        object InvalidName : CreateTunnelFailure()
        object NameAlreadyExists : CreateTunnelFailure()
    }
}