package co.sentinel.dvpn.cache.di

import co.sentinel.dvpn.cache.data.LastSessionDatastore
import co.sentinel.dvpn.cache.repository.HubCacheRepositoryImpl
import co.sentinel.dvpn.domain.features.hub.source.HubCacheRepository
import org.koin.dsl.module


val cacheModule = module {
    single { LastSessionDatastore(get()) }
    single<HubCacheRepository> { HubCacheRepositoryImpl(get()) }
}