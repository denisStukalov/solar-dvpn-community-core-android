package co.sentinel.dvpn.domain.features.registry.source

import co.sentinel.dvpn.domain.core.exception.Failure
import co.sentinel.dvpn.domain.core.functional.Either
import co.sentinel.dvpn.domain.core.interactor.Success
import co.sentinel.dvpn.domain.features.registry.model.Registry

interface RegistryRepository {

    suspend fun deleteRegistry(key: String): Either<Failure, Success>

    suspend fun getRegistry(key: String): Either<Failure, Registry?>

    suspend fun postRegistry(
        key: String,
        value: String,
        isSecure: Boolean
    ): Either<Failure, Success>


}