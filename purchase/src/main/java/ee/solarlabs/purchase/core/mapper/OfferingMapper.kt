package ee.solarlabs.purchase.core.mapper

import co.sentinel.dvpn.domain.features.purchase.model.Offering
import co.sentinel.dvpn.domain.features.purchase.model.PackageType
import co.sentinel.dvpn.domain.features.purchase.model.Package
import co.sentinel.dvpn.domain.features.purchase.model.StoreProduct
import com.revenuecat.purchases.Offerings
import java.util.*

object OfferingMapper {
    private const val MICRO_UNIT = 1000000.00

    fun map(offerings: Offerings): List<Offering> {
        return offerings.all.map { map ->
            val offering = map.value

            Offering(
                identifier = offering.identifier,
                serverDescription = offering.serverDescription,
                availablePackages = offering.availablePackages.map { mapPackage(it) },
                lifetime = offering.lifetime?.let { mapPackage(it) },
                annual = offering.annual?.let { mapPackage(it) },
                sixMonth = offering.sixMonth?.let { mapPackage(it) },
                threeMonth = offering.threeMonth?.let { mapPackage(it) },
                twoMonth = offering.twoMonth?.let { mapPackage(it) },
                monthly = offering.monthly?.let { mapPackage(it) },
                weekly = offering.weekly?.let { mapPackage(it) }
            )

        }
    }

    private fun mapPackage(purchasesPackage: com.revenuecat.purchases.Package): Package =
        Package(
            identifier = purchasesPackage.identifier,
            packageType = mapPackageType(purchasesPackage.packageType),
            storeProduct = mapStoreProduct(purchasesPackage.product),
            offeringIdentifier = purchasesPackage.offering,
            localizedPriceString = purchasesPackage.product.price.replace(",", ".")
        )

    private fun mapPackageType(packageType: com.revenuecat.purchases.PackageType): PackageType =
        when (packageType) {
            com.revenuecat.purchases.PackageType.UNKNOWN -> PackageType.UNKNOWN
            com.revenuecat.purchases.PackageType.CUSTOM -> PackageType.CUSTOM
            com.revenuecat.purchases.PackageType.LIFETIME -> PackageType.LIFETIME
            com.revenuecat.purchases.PackageType.ANNUAL -> PackageType.ANNUAL
            com.revenuecat.purchases.PackageType.SIX_MONTH -> PackageType.SIX_MONTH
            com.revenuecat.purchases.PackageType.THREE_MONTH -> PackageType.THREE_MONTH
            com.revenuecat.purchases.PackageType.TWO_MONTH -> PackageType.TWO_MONTH
            com.revenuecat.purchases.PackageType.MONTHLY -> PackageType.MONTHLY
            com.revenuecat.purchases.PackageType.WEEKLY -> PackageType.WEEKLY
        }

    private fun mapStoreProduct(storeProduct: com.revenuecat.purchases.models.StoreProduct): StoreProduct =
        StoreProduct(
            price = storeProduct.priceAmountMicros.div(MICRO_UNIT),
            currency = Currency.getInstance(storeProduct.priceCurrencyCode).symbol ?: "$"
        )
}