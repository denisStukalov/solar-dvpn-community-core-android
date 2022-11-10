package co.sentinel.cosmos.core.di

import co.sentinel.cosmos.WalletRepositoryImpl
import co.sentinel.dvpn.domain.features.wallet.source.WalletRepository
import org.koin.dsl.module


val walletModule = module {
    single<WalletRepository> { WalletRepositoryImpl(get()) }
}
