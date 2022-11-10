package ee.solarlabs.registry

import co.sentinel.dvpn.domain.core.exception.Failure
import co.sentinel.dvpn.domain.core.functional.Either
import co.sentinel.dvpn.domain.core.interactor.Success
import co.sentinel.dvpn.domain.features.registry.model.Registry
import co.sentinel.dvpn.domain.features.registry.source.RegistryRepository
import ee.solarlabs.registry.core.registry.PlainRegistry
import ee.solarlabs.registry.core.registry.SecureRegistry
import timber.log.Timber

class RegistryRepositoryImpl(
    private val plainRegistry: PlainRegistry,
    private val secureRegistry: SecureRegistry
) : RegistryRepository {


    override suspend fun deleteRegistry(key: String): Either<Failure, Success> =
        kotlin.runCatching {
            plainRegistry.deleteKeyValue(key)
            secureRegistry.deleteKeyValue(key)
            Either.Right(Success)
        }.onFailure { Timber.e(it) }.getOrNull() ?: Either.Left(Failure.AppError)

    override suspend fun getRegistry(key: String): Either<Failure, Registry?> =
        kotlin.runCatching {
            var registry: Registry? = null
            secureRegistry.retrieveKeyValue(key)?.let {
                registry = Registry(
                    key = key,
                    value = it,
                    isSecure = true
                )
            }

            plainRegistry.retrieveKeyValue(key)?.let {
                registry = Registry(
                    key = key,
                    value = it,
                    isSecure = false
                )
            }

            Either.Right(registry)
        }.onFailure { Timber.e(it) }.getOrNull() ?: Either.Left(Failure.AppError)

    override suspend fun postRegistry(
        key: String,
        value: String,
        isSecure: Boolean
    ): Either<Failure, Success> = kotlin.runCatching {
        if (isSecure) {
            secureRegistry.storeKeyValue(key, value)
        } else {
            plainRegistry.storeKeyValue(key, value)
        }
        Either.Right(Success)
    }.onFailure { Timber.e(it) }.getOrNull() ?: Either.Left(Failure.AppError)
}