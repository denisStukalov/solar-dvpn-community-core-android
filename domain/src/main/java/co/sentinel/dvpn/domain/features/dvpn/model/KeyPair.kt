package co.sentinel.dvpn.domain.features.dvpn.model

data class KeyPair(
    val privateKeyHex: String,
    val privateKeyBase64: String,
    val publicKeyHex: String,
    val publicKeyBase64: String
)