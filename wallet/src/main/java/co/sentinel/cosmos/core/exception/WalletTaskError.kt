package co.sentinel.cosmos.core.exception

import co.sentinel.dvpn.domain.core.exception.Failure

/**
 * Wallet GRPC task error. Contains Service GRPC [cosmos.base.abci.v1beta1.Abci.TxResponse]
 * code and error message. If task locally failed, codes [co.sentinel.cosmos.base.BaseConstant.ERROR_CODE_UNKNOWN]
 * in case of a query or [co.sentinel.cosmos.base.BaseConstant.ERROR_CODE_BROADCAST] in case
 * of a broadcast task will be passed with localized throwable message.
 */
class WalletTaskError(val code: Int, val message: String) : Failure.FeatureFailure()