package co.sentinel.dvpn.domain.features.dvpn

import co.sentinel.dvpn.domain.core.exception.Failure
import co.sentinel.dvpn.domain.core.functional.Either
import co.sentinel.dvpn.domain.core.interactor.BaseUseCase
import co.sentinel.dvpn.domain.features.dvpn.source.DVPNRepository

class GetTunnelStatistics(
    private val repository: DVPNRepository
) : BaseUseCase<GetTunnelStatistics.Success, GetTunnelStatistics.GetTunnelStatisticsParams>() {

    override suspend fun run(params: GetTunnelStatisticsParams): Either<Failure, Success> {
        return repository.getTunnelStatistics(params)
    }

    object Success

    data class GetTunnelStatisticsParams(val tunnelName: String)

    sealed class GetTunnelStatisticsFailure : Failure.FeatureFailure() {
        object TunnelNotFound : GetTunnelStatisticsFailure()
        object BackendNotInitialized : GetTunnelStatisticsFailure()
    }
}