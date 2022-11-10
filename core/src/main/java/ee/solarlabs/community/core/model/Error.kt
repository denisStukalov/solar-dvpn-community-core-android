package ee.solarlabs.community.core.model

import com.google.gson.annotations.JsonAdapter
import ee.solarlabs.community.core.util.FailureReasonSerializer
import io.ktor.http.HttpStatusCode

data class ErrorWrapper(
    val error: Error,
    val code: HttpStatusCode = HttpStatusCode.InternalServerError
)

sealed class Error {
    data class GeneralError(
        val reason: String,
        val error: Boolean = true
    ) : Error()

    data class InnerError(
        val error: Boolean = true,
        @JsonAdapter(FailureReasonSerializer::class)
        val reason: FailureReason
    ) : Error() {
        data class FailureReason(
            val message: String,
            val code: Int
        )
    }
}

sealed class HttpError {
    companion object {
        val badRequest = Error.GeneralError("Bad Request") // 400
        val unauthorized = Error.GeneralError("Unauthorized") // 401..402
        val accessDenied = Error.GeneralError("Access Denied") // 403
        val notFound = Error.GeneralError("Not Found") // 404
        val internalServer = Error.GeneralError("Internal Server Error") //500
    }
}

sealed class TunnelServiceError {
    companion object {
        val tunnelEmptyName = Error.GeneralError("empty_name")
        val nameAlreadyExists = Error.GeneralError("name_already_exists")
        val loadTunnelsFailed = Error.GeneralError("load_tunnels_failed")
        val addTunnelFailed = Error.GeneralError("add_tunnel_failed")
        val removeTunnelFailed = Error.GeneralError("remove_tunnel_failed")
        val tunnelNotFound = Error.GeneralError("tunnel_not_found")
    }
}

sealed class TunnelActivationError {
    companion object {
        val inactive = Error.GeneralError("inactive")
        val startingFailed = Error.GeneralError("starting_failed")
        val savingFailed = Error.GeneralError("saving_failed")
        val loadingFailed = Error.GeneralError("loading_failed")
        val retryLimitReached = Error.GeneralError("retry_limit_reached")
        val activationAttemptFailed = Error.GeneralError("activation_attempt_failed")
    }
}

sealed class PurchaseError {
    companion object {
        val purchaseCancelled = Error.GeneralError("purchase_was_canceled")
    }
}

sealed class NodeServiceError {
    companion object {
        val failToLoadData = Error.GeneralError("fail_to_load_data")
    }
}

sealed class SubscriptionsProviderError {
    companion object {
        val broadcastFailed = Error.GeneralError("broadcast_failed")
        val sessionStartFailed = Error.GeneralError("session_start_failed")
        val sessionsStopFailed = Error.GeneralError("sessions_stop_failed")
    }
}

sealed class EncoderError {
    companion object {
        val failToEncode = Error.GeneralError("fail_to_encode")
    }
}

sealed class SessionsServiceError {
    companion object {
        val invalidURL = Error.GeneralError("invalid_url")
        val connectionParsingFailed = Error.GeneralError("connection_parsing_failed")
        val nodeMisconfigured = Error.GeneralError("node_misconfigured")
        val noQuota = Error.GeneralError("no_quota")
    }
}


sealed class TunnelSavingError {
    companion object {
        val nameRequired = Error.GeneralError("name_required")
        val privateKeyRequired = Error.GeneralError("private_key_required")
        val privateKeyInvalid = Error.GeneralError("private_key_invalid")
        val addressInvalid = Error.GeneralError("address_invalid")
        val listenPortInvalid = Error.GeneralError("listen_port_invalid")
        val MTUInvalid = Error.GeneralError("mtu_invalid")
        val publicKeyRequired = Error.GeneralError("public_key_required")
        val publicKeyInvalid = Error.GeneralError("public_key_invalid")
        val preSharedKeyInvalid = Error.GeneralError("pre_shared_key_invalid")
        val allowedIPsInvalid = Error.GeneralError("allowed_ips_invalid")
        val endpointInvalid = Error.GeneralError("endpoint_invalid")
        val persistentKeepAliveInvalid = Error.GeneralError("persistent_keep_alive_invalid")
        val publicKeyDuplicated = Error.GeneralError("public_key_duplicated")
    }
}

sealed class WalletServiceError {
    companion object {
        val accountMatchesDestination = Error.GeneralError("account_matches_destination") // 403
        val missingMnemonics = Error.GeneralError("missing_mnemonics") // 401
        val missingAuthorization = Error.GeneralError("missing_authorization") // 401
        val notEnoughTokens = Error.GeneralError("not_enough_tokens") // 402
        val mnemonicsDoNotMatch = Error.GeneralError("mnemonic_do_not_match") // 401
        val savingError = Error.GeneralError("saving_error") // 500
    }
}

sealed class PacketTunnelProviderError {
    companion object {
        val savedProtocolConfigurationIsInvalid =
            Error.GeneralError("saved_protocol_configuration_is_Invalid")
        val dnsResolutionFailure = Error.GeneralError("dns_resolution_failure")
        val couldNotStartBackend = Error.GeneralError("could_not_start_backend")
        val couldNotDetermineFileDescriptor =
            Error.GeneralError("could_not_determine_file_descriptor")
        val couldNotSetNetworkSettings = Error.GeneralError("could_not_set_network_settings")
    }
}

sealed class SubscriptionsServiceError {
    companion object {
        val missingMnemonic = Error.GeneralError("missing_mnemonic")
        val paymentFailed = Error.GeneralError("payment_failed")
        val failToCancelSubscription = Error.GeneralError("fail_to_cancel_subscription")
        val activeSession = Error.GeneralError("active_session")
    }
}

sealed class ConnectionModelError {
    companion object {
        val signatureGenerationFailed = Error.GeneralError("signature_generation_failed")
        val nodeIsOffline = Error.GeneralError("node_is_offline")
        val balanceUpdateFailed = Error.GeneralError("balance_update_failed")
        val noSubscription = Error.GeneralError("no_subscription")
        val noQuotaLeft = Error.GeneralError("no_quota_left")
        val tunnelIsAlreadyActive = Error.GeneralError("tunnel_is_already_active")
    }
}
