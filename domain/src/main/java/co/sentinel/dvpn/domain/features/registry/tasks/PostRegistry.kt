package co.sentinel.dvpn.domain.features.registry.tasks

import co.sentinel.dvpn.domain.core.exception.Failure
import co.sentinel.dvpn.domain.core.functional.Either
import co.sentinel.dvpn.domain.core.interactor.Success
import co.sentinel.dvpn.domain.features.registry.source.RegistryRepository

class PostRegistry(private val registryRepository: RegistryRepository) {

    suspend operator fun invoke(params: Params): Either<Failure, Success> =
        registryRepository.postRegistry(
            params.key,
            params.value,
            params.isSecure
        )

    data class Params(
        val key: String,
        val value: String,
        val isSecure: Boolean
    )
}