package co.sentinel.dvpn.domain.features.registry.model

data class Registry(
    val key: String,
    val value: String,
    val isSecure: Boolean
)