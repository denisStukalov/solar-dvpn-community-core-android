package co.sentinel.dvpn.domain.features.purchase.model

enum class PackageType(value: Int) {
    UNKNOWN(-2),
    CUSTOM(-1),
    LIFETIME(0),
    ANNUAL(1),
    SIX_MONTH(2),
    THREE_MONTH(3),
    TWO_MONTH(4),
    MONTHLY(5),
    WEEKLY(6)
}