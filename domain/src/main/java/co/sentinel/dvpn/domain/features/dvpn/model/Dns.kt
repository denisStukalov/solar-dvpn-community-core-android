package co.sentinel.dvpn.domain.features.dvpn.model

enum class Dns(name: String) {
    CLOUDFLARE("cloudflare"),
    GOOGLE("google"),
    HANDSHAKE("handshake")
}
