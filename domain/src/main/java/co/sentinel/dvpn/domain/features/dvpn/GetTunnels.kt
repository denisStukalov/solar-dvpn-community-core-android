package co.sentinel.dvpn.domain.features.dvpn

import co.sentinel.dvpn.domain.core.exception.Failure
import co.sentinel.dvpn.domain.core.functional.Either
import co.sentinel.dvpn.domain.core.interactor.BaseUseCaseNoParams
import co.sentinel.dvpn.domain.features.dvpn.model.DvpnTunnel
import co.sentinel.dvpn.domain.features.dvpn.source.DVPNRepository

class GetTunnels(
    private val repository: DVPNRepository
) : BaseUseCaseNoParams<GetTunnels.Success>() {

    override suspend fun run(): Either<Failure, Success> {
        return repository.getTunnels()
    }

    data class Success(val tunnels: List<DvpnTunnel>)
}