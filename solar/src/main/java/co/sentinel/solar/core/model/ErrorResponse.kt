package co.sentinel.solar.core.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ErrorResponse(
    val errors: List<Error>
)

@JsonClass(generateAdapter = true)
data class Error(
    val code: Int,
    val message: String
)