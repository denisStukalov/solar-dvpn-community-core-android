package co.sentinel.dvpn.domain.core

import co.sentinel.cosmos.model.type.Coin
import co.sentinel.cosmos.model.type.Fee

const val mainDenom = "DVPN"
const val denom = "udvpn"
const val DEFAULT_FEE_AMOUNT = 10000L
const val UDVPN_MULTIPLIER = 0.000001
const val DEFAULT_GAS = 100000
val DEFAULT_FEE =
    Fee(DEFAULT_GAS.toString(), arrayListOf(Coin(denom, DEFAULT_FEE_AMOUNT.toString())))

const val DVPN_NODE_NAME = "SolarDVPN"

fun generateCoinFromPrice(price: String) = price.let {
    val denom: String = it.replace("[^A-Za-z]".toRegex(), "")
    val amount: String = it.replace("[^0-9]".toRegex(), "")
    Coin(denom, amount)
}

fun generateDVPNPrice(price: Long, fee: Long) = price.let {
    String.format("%.6f", (price + fee) * UDVPN_MULTIPLIER)
}