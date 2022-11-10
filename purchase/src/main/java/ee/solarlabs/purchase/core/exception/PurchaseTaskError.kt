package ee.solarlabs.purchase.core.exception

import co.sentinel.dvpn.domain.core.exception.Failure

/**
 * Purchase error. This is a mapped [com.revenuecat.purchases.PurchasesError]
 * returned on [onError] revenuecat callback. For detailed error code and description list
 * check [com.revenuecat.purchases.PurchasesErrorCode].
 */
data class PurchaseTaskError(
    val errorCode: Int,
    val description: String,
    val underlyingErrorMessage: String?
) : Failure.FeatureFailure()