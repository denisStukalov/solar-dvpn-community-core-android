package co.sentinel.solar.core.di

import co.sentinel.dvpn.domain.features.solar.source.SolarRepository
import co.sentinel.solar.SolarRepositoryImpl
import co.sentinel.solar.core.api.DVPNService
import co.sentinel.solar.core.api.config.DVPNServiceFactory
import org.koin.dsl.module

val solarModule = module {
    single<DVPNService> { DVPNServiceFactory.makeService() }
    single<SolarRepository> { SolarRepositoryImpl(get(), get()) }
}