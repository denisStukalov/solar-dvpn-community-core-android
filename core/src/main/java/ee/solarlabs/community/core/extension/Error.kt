package ee.solarlabs.community.core.extension

import co.sentinel.cosmos.core.exception.WalletTaskError
import co.sentinel.dvpn.hub.exception.HubTaskError
import ee.solarlabs.community.core.model.Error
import ee.solarlabs.purchase.core.exception.PurchaseTaskError


fun HubTaskError.toError() = Error.InnerError(
    error = true,
    reason = Error.InnerError.FailureReason(
        message = this.message,
        code = this.code
    )
)

fun WalletTaskError.toError() = Error.InnerError(
    error = true,
    reason = Error.InnerError.FailureReason(
        message = this.message,
        code = this.code
    )
)

fun PurchaseTaskError.toError() = Error.InnerError(
    error = true,
    reason = Error.InnerError.FailureReason(
        message = this.description,
        code = this.errorCode
    )
)