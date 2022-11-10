package co.sentinel.dvpn.domain.features.wallet.model

data class Account(
    val wallet: Wallet,
    val mnemonics: String
)