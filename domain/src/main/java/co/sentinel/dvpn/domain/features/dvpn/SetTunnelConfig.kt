package co.sentinel.dvpn.domain.features.dvpn

import co.sentinel.dvpn.domain.core.exception.Failure
import co.sentinel.dvpn.domain.core.functional.Either
import co.sentinel.dvpn.domain.core.interactor.BaseUseCase
import co.sentinel.dvpn.domain.features.dvpn.model.TunnelConfig
import co.sentinel.dvpn.domain.features.dvpn.source.DVPNRepository

class SetTunnelConfig(
    private val repository: DVPNRepository
) : BaseUseCase<SetTunnelConfig.Success, SetTunnelConfig.SetTunnelConfigParams>() {

    override suspend fun run(params: SetTunnelConfigParams): Either<Failure, Success> {
        return repository.setTunnelConfig(params)
    }

    object Success

    data class SetTunnelConfigParams(val tunnelName: String, val config: TunnelConfig)

    sealed class SetTunnelConfigFailure: Failure.FeatureFailure() {
        object TunnelNotFound : SetTunnelConfigFailure()
        object BackendNotInitialized : SetTunnelConfigFailure()
    }
}