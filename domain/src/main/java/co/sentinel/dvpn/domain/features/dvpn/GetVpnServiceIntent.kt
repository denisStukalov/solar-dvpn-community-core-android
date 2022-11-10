package co.sentinel.dvpn.domain.features.dvpn

import android.content.Context
import co.sentinel.dvpn.domain.core.exception.Failure
import co.sentinel.dvpn.domain.features.dvpn.source.DVPNRepository
import java.lang.ref.WeakReference

class GetVpnServiceIntent(
    private val repository: DVPNRepository
) {
    operator fun invoke(params: Context) = repository.getVpnServiceIntent(WeakReference(params))

    sealed class GetVpnServiceIntentFailure : Failure.FeatureFailure() {
        object NotGoBackendError : GetVpnServiceIntentFailure()
    }
}