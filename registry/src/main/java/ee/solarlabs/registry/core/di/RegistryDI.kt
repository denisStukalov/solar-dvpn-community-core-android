package ee.solarlabs.registry.core.di

import co.sentinel.dvpn.domain.features.registry.source.RegistryRepository
import ee.solarlabs.registry.RegistryRepositoryImpl
import ee.solarlabs.registry.core.registry.PlainRegistry
import ee.solarlabs.registry.core.registry.SecureRegistry
import org.koin.dsl.module

val registryModule = module {
    single { PlainRegistry(get()) }
    single { SecureRegistry(get()) }
    single<RegistryRepository> { RegistryRepositoryImpl(get(), get()) }
}