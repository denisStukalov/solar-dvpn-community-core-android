package co.sentinel.dvpn.domain.features.wallet.model

data class Wallet(
    val address: String,
    val balance: Int,
    val currency: String
)