package co.sentinel.dvpn.hub.exception

import co.sentinel.dvpn.domain.core.exception.Failure
import io.grpc.Status
import io.grpc.TlsServerCredentials.Feature

/**
 * Hub/sentinel GRPC task error. Contains GRPC [io.grpc.Status] code and message
 * contained in [io.grpc.StatusRuntimeException]. If task locally failed, default
 * [co.sentinel.dvpn.hub.exception.Util.TASK_FAILED_CODE] value will be passed as a code.
 */
class HubTaskError(val code: Int, val message: String) : Failure.FeatureFailure()

const val TASK_FAILED_CODE = -1

fun formatHubTaskError(t: Throwable): HubTaskError {
    return Status.fromThrowable(t)?.let { status ->
        HubTaskError(
            status.code.value(),
            status.description ?: "GRPC error occurred."
        )
    } ?: HubTaskError(
        TASK_FAILED_CODE,
        t.localizedMessage ?: "GRPC task error occurred."
    )
}