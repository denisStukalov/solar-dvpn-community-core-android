package co.sentinel.dvpn.domain.features.dvpn

import co.sentinel.dvpn.domain.core.exception.Failure
import co.sentinel.dvpn.domain.core.functional.Either
import co.sentinel.dvpn.domain.core.interactor.BaseUseCase
import co.sentinel.dvpn.domain.features.dvpn.model.DvpnTunnel
import co.sentinel.dvpn.domain.features.dvpn.source.DVPNRepository

class SetTunnelName(
    private val repository: DVPNRepository
) : BaseUseCase<SetTunnelName.Success, SetTunnelName.SetTunnelNameParams>() {

    override suspend fun run(params: SetTunnelNameParams): Either<Failure, Success> {
        return repository.setTunnelName(params)
    }

    object Success

    data class SetTunnelNameParams(val newTunnelName: String, val tunnel: DvpnTunnel)

    sealed class SetTunnelNameFailure : Failure.FeatureFailure() {
        object InvalidName : SetTunnelNameFailure()
        object NameAlreadyExists : SetTunnelNameFailure()
        object SetNameFailed : SetTunnelNameFailure()
    }
}