package co.sentinel.dvpn.domain.features.dvpn

import co.sentinel.dvpn.domain.core.exception.Failure
import co.sentinel.dvpn.domain.core.functional.Either
import co.sentinel.dvpn.domain.core.interactor.BaseUseCase
import co.sentinel.dvpn.domain.features.dvpn.model.TunnelConfig
import co.sentinel.dvpn.domain.features.dvpn.source.DVPNRepository

class GetTunnelConfig(
    private val repository: DVPNRepository
) : BaseUseCase<GetTunnelConfig.Success, GetTunnelConfig.GetTunnelConfigParams>() {

    override suspend fun run(params: GetTunnelConfigParams): Either<Failure, Success> {
        return repository.getTunnelConfig(params)
    }

    data class GetTunnelConfigParams(val tunnelName: String)

    data class Success(val config: TunnelConfig)

    sealed class GetTunnelConfigFailure : Failure.FeatureFailure() {
        object TunnelNotFound : GetTunnelConfigFailure()
    }
}