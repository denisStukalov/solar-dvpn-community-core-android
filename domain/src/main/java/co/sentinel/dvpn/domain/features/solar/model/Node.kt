package co.sentinel.dvpn.domain.features.solar.model

import java.math.BigInteger

data class Node(
    val id: Long,
    val blockchainAddress: String,
    val isTrusted: Boolean,
    val moniker: String?,
    val remoteUrl: String,
    val status: String,
    val defaultPrice: BigInteger,
    val bandwidthUpload: Int,
    val bandwidthDownload: Int,
    val locationCountryCode: String,
    val locationContinentCode: String
)