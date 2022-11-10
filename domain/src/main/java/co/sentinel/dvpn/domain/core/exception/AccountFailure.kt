package co.sentinel.dvpn.domain.core.exception

/**
 * Error thrown when account is not found in local database. Check [co.sentinel.cosmos.base.BaseData]
 * and [co.sentinel.dvpn.domain.features.wallet.source.WalletRepository.getAccount].
 */
object AccountError : Failure.FeatureFailure()