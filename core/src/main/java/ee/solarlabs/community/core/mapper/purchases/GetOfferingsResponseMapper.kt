package ee.solarlabs.community.core.mapper.purchases

import co.sentinel.dvpn.domain.core.functional.Mapper
import co.sentinel.dvpn.domain.features.purchase.model.Offering
import co.sentinel.dvpn.domain.features.purchase.model.PackageType
import ee.solarlabs.community.core.model.purchases.response.GetOfferingsResponse

object GetOfferingsResponseMapper : Mapper<Offering, GetOfferingsResponse> {
    override fun map(obj: Offering) = GetOfferingsResponse(
        identifier = obj.identifier,
        serverDescription = obj.serverDescription,
        availablePackages = obj.availablePackages.map {
            GetOfferingsResponse.Package(
                identifier = it.identifier,
                packageType = when (it.packageType) {
                    PackageType.UNKNOWN -> GetOfferingsResponse.Package.PackageType.UNKNOWN
                    PackageType.CUSTOM -> GetOfferingsResponse.Package.PackageType.CUSTOM
                    PackageType.LIFETIME -> GetOfferingsResponse.Package.PackageType.LIFETIME
                    PackageType.ANNUAL -> GetOfferingsResponse.Package.PackageType.ANNUAL
                    PackageType.SIX_MONTH -> GetOfferingsResponse.Package.PackageType.SIX_MONTH
                    PackageType.THREE_MONTH -> GetOfferingsResponse.Package.PackageType.THREE_MONTH
                    PackageType.TWO_MONTH -> GetOfferingsResponse.Package.PackageType.TWO_MONTH
                    PackageType.MONTHLY -> GetOfferingsResponse.Package.PackageType.MONTHLY
                    PackageType.WEEKLY -> GetOfferingsResponse.Package.PackageType.WEEKLY
                },
                storeProduct = GetOfferingsResponse.Package.StoreProduct(
                    price = it.storeProduct.price,
                    currency = it.storeProduct.currency
                ),
                offeringIdentifier = it.offeringIdentifier,
                localizedPriceString = it.localizedPriceString
            )
        },
        lifetime = obj.lifetime?.let {
            GetOfferingsResponse.Package(
                identifier = it.identifier,
                packageType = when (it.packageType) {
                    PackageType.UNKNOWN -> GetOfferingsResponse.Package.PackageType.UNKNOWN
                    PackageType.CUSTOM -> GetOfferingsResponse.Package.PackageType.CUSTOM
                    PackageType.LIFETIME -> GetOfferingsResponse.Package.PackageType.LIFETIME
                    PackageType.ANNUAL -> GetOfferingsResponse.Package.PackageType.ANNUAL
                    PackageType.SIX_MONTH -> GetOfferingsResponse.Package.PackageType.SIX_MONTH
                    PackageType.THREE_MONTH -> GetOfferingsResponse.Package.PackageType.THREE_MONTH
                    PackageType.TWO_MONTH -> GetOfferingsResponse.Package.PackageType.TWO_MONTH
                    PackageType.MONTHLY -> GetOfferingsResponse.Package.PackageType.MONTHLY
                    PackageType.WEEKLY -> GetOfferingsResponse.Package.PackageType.WEEKLY
                },
                storeProduct = GetOfferingsResponse.Package.StoreProduct(
                    price = it.storeProduct.price,
                    currency = it.storeProduct.currency
                ),
                offeringIdentifier = it.offeringIdentifier,
                localizedPriceString = it.localizedPriceString
            )
        },
        annual = obj.annual?.let {
            GetOfferingsResponse.Package(
                identifier = it.identifier,
                packageType = when (it.packageType) {
                    PackageType.UNKNOWN -> GetOfferingsResponse.Package.PackageType.UNKNOWN
                    PackageType.CUSTOM -> GetOfferingsResponse.Package.PackageType.CUSTOM
                    PackageType.LIFETIME -> GetOfferingsResponse.Package.PackageType.LIFETIME
                    PackageType.ANNUAL -> GetOfferingsResponse.Package.PackageType.ANNUAL
                    PackageType.SIX_MONTH -> GetOfferingsResponse.Package.PackageType.SIX_MONTH
                    PackageType.THREE_MONTH -> GetOfferingsResponse.Package.PackageType.THREE_MONTH
                    PackageType.TWO_MONTH -> GetOfferingsResponse.Package.PackageType.TWO_MONTH
                    PackageType.MONTHLY -> GetOfferingsResponse.Package.PackageType.MONTHLY
                    PackageType.WEEKLY -> GetOfferingsResponse.Package.PackageType.WEEKLY
                },
                storeProduct = GetOfferingsResponse.Package.StoreProduct(
                    price = it.storeProduct.price,
                    currency = it.storeProduct.currency
                ),
                offeringIdentifier = it.offeringIdentifier,
                localizedPriceString = it.localizedPriceString
            )
        },
        sixMonth = obj.sixMonth?.let {
            GetOfferingsResponse.Package(
                identifier = it.identifier,
                packageType = when (it.packageType) {
                    PackageType.UNKNOWN -> GetOfferingsResponse.Package.PackageType.UNKNOWN
                    PackageType.CUSTOM -> GetOfferingsResponse.Package.PackageType.CUSTOM
                    PackageType.LIFETIME -> GetOfferingsResponse.Package.PackageType.LIFETIME
                    PackageType.ANNUAL -> GetOfferingsResponse.Package.PackageType.ANNUAL
                    PackageType.SIX_MONTH -> GetOfferingsResponse.Package.PackageType.SIX_MONTH
                    PackageType.THREE_MONTH -> GetOfferingsResponse.Package.PackageType.THREE_MONTH
                    PackageType.TWO_MONTH -> GetOfferingsResponse.Package.PackageType.TWO_MONTH
                    PackageType.MONTHLY -> GetOfferingsResponse.Package.PackageType.MONTHLY
                    PackageType.WEEKLY -> GetOfferingsResponse.Package.PackageType.WEEKLY
                },
                storeProduct = GetOfferingsResponse.Package.StoreProduct(
                    price = it.storeProduct.price,
                    currency = it.storeProduct.currency
                ),
                offeringIdentifier = it.offeringIdentifier,
                localizedPriceString = it.localizedPriceString
            )
        },
        threeMonth = obj.threeMonth?.let {
            GetOfferingsResponse.Package(
                identifier = it.identifier,
                packageType = when (it.packageType) {
                    PackageType.UNKNOWN -> GetOfferingsResponse.Package.PackageType.UNKNOWN
                    PackageType.CUSTOM -> GetOfferingsResponse.Package.PackageType.CUSTOM
                    PackageType.LIFETIME -> GetOfferingsResponse.Package.PackageType.LIFETIME
                    PackageType.ANNUAL -> GetOfferingsResponse.Package.PackageType.ANNUAL
                    PackageType.SIX_MONTH -> GetOfferingsResponse.Package.PackageType.SIX_MONTH
                    PackageType.THREE_MONTH -> GetOfferingsResponse.Package.PackageType.THREE_MONTH
                    PackageType.TWO_MONTH -> GetOfferingsResponse.Package.PackageType.TWO_MONTH
                    PackageType.MONTHLY -> GetOfferingsResponse.Package.PackageType.MONTHLY
                    PackageType.WEEKLY -> GetOfferingsResponse.Package.PackageType.WEEKLY
                },
                storeProduct = GetOfferingsResponse.Package.StoreProduct(
                    price = it.storeProduct.price,
                    currency = it.storeProduct.currency
                ),
                offeringIdentifier = it.offeringIdentifier,
                localizedPriceString = it.localizedPriceString
            )
        },
        twoMonth = obj.twoMonth?.let {
            GetOfferingsResponse.Package(
                identifier = it.identifier,
                packageType = when (it.packageType) {
                    PackageType.UNKNOWN -> GetOfferingsResponse.Package.PackageType.UNKNOWN
                    PackageType.CUSTOM -> GetOfferingsResponse.Package.PackageType.CUSTOM
                    PackageType.LIFETIME -> GetOfferingsResponse.Package.PackageType.LIFETIME
                    PackageType.ANNUAL -> GetOfferingsResponse.Package.PackageType.ANNUAL
                    PackageType.SIX_MONTH -> GetOfferingsResponse.Package.PackageType.SIX_MONTH
                    PackageType.THREE_MONTH -> GetOfferingsResponse.Package.PackageType.THREE_MONTH
                    PackageType.TWO_MONTH -> GetOfferingsResponse.Package.PackageType.TWO_MONTH
                    PackageType.MONTHLY -> GetOfferingsResponse.Package.PackageType.MONTHLY
                    PackageType.WEEKLY -> GetOfferingsResponse.Package.PackageType.WEEKLY
                },
                storeProduct = GetOfferingsResponse.Package.StoreProduct(
                    price = it.storeProduct.price,
                    currency = it.storeProduct.currency
                ),
                offeringIdentifier = it.offeringIdentifier,
                localizedPriceString = it.localizedPriceString
            )
        },
        monthly = obj.monthly?.let {
            GetOfferingsResponse.Package(
                identifier = it.identifier,
                packageType = when (it.packageType) {
                    PackageType.UNKNOWN -> GetOfferingsResponse.Package.PackageType.UNKNOWN
                    PackageType.CUSTOM -> GetOfferingsResponse.Package.PackageType.CUSTOM
                    PackageType.LIFETIME -> GetOfferingsResponse.Package.PackageType.LIFETIME
                    PackageType.ANNUAL -> GetOfferingsResponse.Package.PackageType.ANNUAL
                    PackageType.SIX_MONTH -> GetOfferingsResponse.Package.PackageType.SIX_MONTH
                    PackageType.THREE_MONTH -> GetOfferingsResponse.Package.PackageType.THREE_MONTH
                    PackageType.TWO_MONTH -> GetOfferingsResponse.Package.PackageType.TWO_MONTH
                    PackageType.MONTHLY -> GetOfferingsResponse.Package.PackageType.MONTHLY
                    PackageType.WEEKLY -> GetOfferingsResponse.Package.PackageType.WEEKLY
                },
                storeProduct = GetOfferingsResponse.Package.StoreProduct(
                    price = it.storeProduct.price,
                    currency = it.storeProduct.currency
                ),
                offeringIdentifier = it.offeringIdentifier,
                localizedPriceString = it.localizedPriceString
            )
        },
        weekly = obj.weekly?.let {
            GetOfferingsResponse.Package(
                identifier = it.identifier,
                packageType = when (it.packageType) {
                    PackageType.UNKNOWN -> GetOfferingsResponse.Package.PackageType.UNKNOWN
                    PackageType.CUSTOM -> GetOfferingsResponse.Package.PackageType.CUSTOM
                    PackageType.LIFETIME -> GetOfferingsResponse.Package.PackageType.LIFETIME
                    PackageType.ANNUAL -> GetOfferingsResponse.Package.PackageType.ANNUAL
                    PackageType.SIX_MONTH -> GetOfferingsResponse.Package.PackageType.SIX_MONTH
                    PackageType.THREE_MONTH -> GetOfferingsResponse.Package.PackageType.THREE_MONTH
                    PackageType.TWO_MONTH -> GetOfferingsResponse.Package.PackageType.TWO_MONTH
                    PackageType.MONTHLY -> GetOfferingsResponse.Package.PackageType.MONTHLY
                    PackageType.WEEKLY -> GetOfferingsResponse.Package.PackageType.WEEKLY
                },
                storeProduct = GetOfferingsResponse.Package.StoreProduct(
                    price = it.storeProduct.price,
                    currency = it.storeProduct.currency
                ),
                offeringIdentifier = it.offeringIdentifier,
                localizedPriceString = it.localizedPriceString
            )
        },
    )
}