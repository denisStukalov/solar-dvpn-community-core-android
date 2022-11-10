package co.sentinel.dvpn.domain.features.dvpn

import co.sentinel.dvpn.domain.core.exception.Failure
import co.sentinel.dvpn.domain.core.functional.Either
import co.sentinel.dvpn.domain.core.interactor.BaseUseCase
import co.sentinel.dvpn.domain.features.dvpn.source.DVPNRepository

class RestoreState(
    private val repository: DVPNRepository
) : BaseUseCase<RestoreState.Success, RestoreState.RestoreStateParams>() {

    override suspend fun run(params: RestoreStateParams): Either<Failure, Success> {
        return repository.restoreState(params).let { Either.Right(Success) }
    }

    data class RestoreStateParams(val force: Boolean)

    object Success

}