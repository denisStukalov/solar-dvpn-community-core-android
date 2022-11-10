package co.sentinel.dvpn.domain.core.exception

/**
 * Base Class for handling errors/failures/exceptions.
 * Every feature specific failure should extend [FeatureFailure] class.
 */
sealed class Failure {
    /**
     * Generic network connection error.
     */
    object NetworkConnection : Failure()

    /**
     * Generic server error. For more specific ones, extend [FeatureFailure].
     */
    object ServerError : Failure()

    /**
     * Generic api error. For more specific ones, extend [FeatureFailure].
     */
    object ApiError : Failure()

    /**
     * Generic app error. For more specific ones, extend [FeatureFailure].
     */
    object AppError : Failure()

    /**
     * Extend this class for feature specific failures.
     */
    abstract class FeatureFailure : Failure()
}
