package co.sentinel.solar.core.exception

import co.sentinel.dvpn.domain.core.exception.Failure
import co.sentinel.solar.core.model.ErrorResponse

data class SolarError(val code: Int?, val error: ErrorResponse?) : Failure.FeatureFailure()