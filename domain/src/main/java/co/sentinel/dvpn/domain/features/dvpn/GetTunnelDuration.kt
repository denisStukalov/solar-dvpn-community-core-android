package co.sentinel.dvpn.domain.features.dvpn

import co.sentinel.dvpn.domain.core.exception.Failure
import co.sentinel.dvpn.domain.core.functional.Either
import co.sentinel.dvpn.domain.core.interactor.BaseUseCase
import co.sentinel.dvpn.domain.features.dvpn.source.DVPNRepository

class GetTunnelDuration(
    private val repository: DVPNRepository
) : BaseUseCase<GetTunnelDuration.Success, GetTunnelDuration.GetTunnelDurationParams>() {

    override suspend fun run(params: GetTunnelDurationParams): Either<Failure, Success> {
        return repository.getTunnelDuration(params.subscriptionId)
    }

    data class GetTunnelDurationParams(val subscriptionId: Long?)

    data class Success(val duration: Long?)
}