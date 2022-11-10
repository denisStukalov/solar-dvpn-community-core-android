package ee.solarlabs.purchase.core.di

import co.sentinel.dvpn.domain.features.purchase.source.PurchaseRepository
import ee.solarlabs.purchase.PurchaseRepositoryImpl
import org.koin.dsl.module

val purchaseModule = module {
    single<PurchaseRepository> { PurchaseRepositoryImpl(get()) }
}