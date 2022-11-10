package co.sentinel.dvpn.domain.features.purchase.model

data class Package(
    val identifier: String,
    val packageType: PackageType,
    val storeProduct: StoreProduct,
    val offeringIdentifier: String,
    val localizedPriceString: String
)