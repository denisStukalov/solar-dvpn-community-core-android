package co.sentinel.dvpn.domain.features.dvpn

import co.sentinel.dvpn.domain.core.exception.Failure
import co.sentinel.dvpn.domain.core.functional.Either
import co.sentinel.dvpn.domain.core.interactor.BaseUseCase
import co.sentinel.dvpn.domain.features.dvpn.source.DVPNRepository

class InitBackend(
    private val repository: DVPNRepository
) : BaseUseCase<InitBackend.Success, InitBackend.InitBackendParams>() {

    override suspend fun run(params: InitBackendParams): Either<Failure, Success> {
        return repository.init(params).let {
            Either.Right(Success)
        }
    }

    data class InitBackendParams(val userAgent: String, val alwaysOnCallback: () -> Unit)

    object Success

}