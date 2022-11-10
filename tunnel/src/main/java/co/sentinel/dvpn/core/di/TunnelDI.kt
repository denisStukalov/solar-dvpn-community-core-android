package co.sentinel.dvpn.core.di

import co.sentinel.dvpn.DVPNRepositoryImpl
import co.sentinel.dvpn.core.store.ConfigStore
import co.sentinel.dvpn.core.store.ConnectionDurationStore
import co.sentinel.dvpn.core.store.ConnectionDurationStoreImpl
import co.sentinel.dvpn.core.store.FileConfigStore
import co.sentinel.dvpn.core.store.TunnelCacheStore
import co.sentinel.dvpn.core.store.TunnelCacheStoreImpl
import co.sentinel.dvpn.core.store.UserPreferenceStore
import co.sentinel.dvpn.core.store.UserPreferenceStoreImpl
import co.sentinel.dvpn.domain.features.dvpn.source.DVPNRepository
import org.koin.dsl.module


val tunnelModule = module {
    single<ConfigStore> { FileConfigStore(get()) }
    single<TunnelCacheStore> { TunnelCacheStoreImpl() }
    single<UserPreferenceStore> { UserPreferenceStoreImpl(get()) }
    single<ConnectionDurationStore> { ConnectionDurationStoreImpl(get()) }

    single<DVPNRepository> { DVPNRepositoryImpl(get(), get(), get(), get(), get(), get()) }

}