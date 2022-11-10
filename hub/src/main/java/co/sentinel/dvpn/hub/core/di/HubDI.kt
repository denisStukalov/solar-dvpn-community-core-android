package co.sentinel.dvpn.hub.core.di

import co.sentinel.dvpn.domain.features.hub.source.HubRemoteRepository
import co.sentinel.dvpn.hub.HubRemoteRepositoryImpl
import org.koin.dsl.module

val hubModule = module {
    single<HubRemoteRepository> { HubRemoteRepositoryImpl(get()) }
}