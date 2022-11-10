package co.sentinel.dvpn.domain.features.hub.failure

import co.sentinel.dvpn.domain.core.exception.Failure

sealed class VpnProfileFailure : Failure.FeatureFailure() {
    object VpnProfileFetchFailure : VpnProfileFailure()
    object VpnProfileRequestFailure : VpnProfileFailure()
    object VpnProfileNodeConfigurationFailure : VpnProfileFailure()
    object VPNProfileNodeServiceUnavailableFailure : VpnProfileFailure()
    object VpnProfileSessionFailure : VpnProfileFailure()
    object VpnProfileNoQuotaFailure : VpnProfileFailure()
    object VpnProfileQuotaExceededFailure : VpnProfileFailure()
    object VpnProfileMaximumPeerReachedFailure : VpnProfileFailure()
    object WalletInsufficientFundsFailure : VpnProfileFailure()
}