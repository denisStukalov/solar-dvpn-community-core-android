package co.sentinel.dvpn.domain.features.purchase.model

data class Offering(
    val identifier: String,
    val serverDescription: String,
    val availablePackages: List<Package>,
    val lifetime: Package?,
    val annual: Package?,
    val sixMonth: Package?,
    val threeMonth: Package?,
    val twoMonth: Package?,
    val monthly: Package?,
    val weekly: Package?
)