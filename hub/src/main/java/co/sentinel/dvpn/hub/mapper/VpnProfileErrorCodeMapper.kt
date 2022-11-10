package co.sentinel.dvpn.hub.mapper

import co.sentinel.dvpn.domain.core.functional.Mapper
import co.sentinel.dvpn.domain.features.hub.failure.VpnProfileFailure
import co.sentinel.dvpn.hub.failure.*

object VpnProfileErrorCodeMapper : Mapper<Int, VpnProfileFailure> {
    override fun map(obj: Int): VpnProfileFailure {
        return when (obj) {
            REQUEST_BODY_DATA_VALIDATION_FAILED,
            REQUEST_BODY_VALIDATION_FAILED -> VpnProfileFailure.VpnProfileRequestFailure
            SESSION_VALIDATION_FAILED,
            SUBSCRIPTION_VALIDATION_FAILED,
            NODE_VALIDATION_FAILED -> VpnProfileFailure.VpnProfileNodeConfigurationFailure
            DUPLICATED_SESSION_REQUEST,
            QUERY_SESSION_FAILED,
            REMOVE_PEER_FAILED -> VpnProfileFailure.VpnProfileSessionFailure
            QUERY_QUOTA_FAILED -> VpnProfileFailure.VpnProfileNoQuotaFailure
            QUOTA_EXCEEDED -> VpnProfileFailure.VpnProfileQuotaExceededFailure
            MAX_PEERS_LIMIT_REACHED -> VpnProfileFailure.VpnProfileMaximumPeerReachedFailure
            else -> VpnProfileFailure.VpnProfileFetchFailure
        }
    }
}