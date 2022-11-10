package co.sentinel.dvpn.domain.features.registry.tasks

import co.sentinel.dvpn.domain.core.exception.Failure
import co.sentinel.dvpn.domain.core.functional.Either
import co.sentinel.dvpn.domain.features.registry.model.Registry
import co.sentinel.dvpn.domain.features.registry.source.RegistryRepository

class GetRegistry(private val registryRegistry: RegistryRepository) {

    suspend operator fun invoke(params: Params): Either<Failure, Registry?> =
        registryRegistry.getRegistry(params.key)

    data class Params(val key: String)
}