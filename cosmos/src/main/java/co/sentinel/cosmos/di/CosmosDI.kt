package co.sentinel.cosmos.di

import co.sentinel.cosmos.base.BaseCosmosApp
import co.sentinel.cosmos.base.BaseData
import org.koin.dsl.module

val cosmosModule = module {
    single<BaseData> { BaseData(get()) }
    single<BaseCosmosApp> { BaseCosmosApp(get(), get()) }
}