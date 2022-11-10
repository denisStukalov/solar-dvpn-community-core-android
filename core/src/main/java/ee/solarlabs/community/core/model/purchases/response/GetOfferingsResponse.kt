package ee.solarlabs.community.core.model.purchases.response


data class GetOfferingsResponse(
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
) {
    data class Package(
        val identifier: String,
        val packageType: PackageType,
        val storeProduct: StoreProduct,
        val offeringIdentifier: String,
        val localizedPriceString: String
    ) {
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

        data class StoreProduct(
            val price: Double,
            val currency: String
        )
    }
}