package co.sentinel.dvpn.domain.features.registry.tasks

import co.sentinel.dvpn.domain.core.exception.Failure
import co.sentinel.dvpn.domain.core.functional.Either
import co.sentinel.dvpn.domain.core.interactor.Success
import co.sentinel.dvpn.domain.features.registry.source.RegistryRepository

class DeleteRegistry(private val registryRepository: RegistryRepository) {

    suspend operator fun invoke(params: Params): Either<Failure, Success> =
        registryRepository.deleteRegistry(params.key)

    data class Params(val key: String)
}