package co.sentinel.dvpn.domain.features.hub.model

import java.time.Instant

data class Subscription(
    val id: Long,
    val node: String,
    val owner: String,
    val price: Coin,
    val deposit: Coin,
    val plan: Long,
    val denom: String,
    val expirationDate: Instant,
    var isActive: Boolean
)