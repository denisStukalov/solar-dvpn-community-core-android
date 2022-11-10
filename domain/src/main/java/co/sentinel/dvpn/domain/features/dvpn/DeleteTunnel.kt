package co.sentinel.dvpn.domain.features.dvpn

import co.sentinel.dvpn.domain.core.exception.Failure
import co.sentinel.dvpn.domain.core.functional.Either
import co.sentinel.dvpn.domain.core.interactor.BaseUseCase
import co.sentinel.dvpn.domain.features.dvpn.source.DVPNRepository

class DeleteTunnel(private val repository: DVPNRepository) :
    BaseUseCase<DeleteTunnel.Success, DeleteTunnel.DeleteTunnelParams>() {

    override suspend fun run(params: DeleteTunnelParams): Either<Failure, Success> {
        return repository.delete(params)
    }

    data class DeleteTunnelParams(val name: String)

    object Success

    sealed class DeleteTunnelFailure : Failure.FeatureFailure() {
        object TunnelNotDeleted : DeleteTunnelFailure()
    }
}