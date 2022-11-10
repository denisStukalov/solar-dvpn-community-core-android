package co.sentinel.solar.core.util

import co.sentinel.dvpn.domain.core.exception.Failure
import co.sentinel.dvpn.domain.core.functional.Either
import co.sentinel.solar.core.exception.SolarError
import co.sentinel.solar.core.model.ErrorResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.HttpException
import java.io.IOException

suspend fun <T> safeApiCall(
    apiCall: suspend () -> T
): Either<Failure, T> {
    return try {
        Either.Right(apiCall.invoke())
    } catch (throwable: Throwable) {
        when (throwable) {
            is IOException -> Either.Left(Failure.NetworkConnection)
            is HttpException -> {
                val code = throwable.code()
                val errorResponse = convertErrorBody(throwable)
                Either.Left(SolarError(code, errorResponse))
            }
            else -> {
                Either.Left(Failure.ApiError)
            }
        }
    }
}

private fun convertErrorBody(throwable: HttpException): ErrorResponse? {
    return try {
        throwable.response()?.errorBody()?.source()?.let {
            val moshiAdapter = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
                .adapter(ErrorResponse::class.java)
            moshiAdapter.fromJson(it)
        }
    } catch (exception: Exception) {
        null
    }
}