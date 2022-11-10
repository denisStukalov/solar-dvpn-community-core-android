package co.sentinel.dvpn.domain.features.dvpn

import co.sentinel.dvpn.domain.core.exception.Failure
import co.sentinel.dvpn.domain.core.functional.Either
import co.sentinel.dvpn.domain.core.interactor.BaseUseCaseNoParams
import co.sentinel.dvpn.domain.features.dvpn.source.DVPNRepository

class LoadTunnels(
    private val repository: DVPNRepository
) : BaseUseCaseNoParams<LoadTunnels.Success>() {

    override suspend fun run(): Either<Failure, Success> {
        return repository.loadTunnels()
    }

    object Success
}