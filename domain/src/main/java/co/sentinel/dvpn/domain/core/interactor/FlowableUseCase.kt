package co.sentinel.dvpn.domain.core.interactor

import co.sentinel.dvpn.domain.core.exception.Failure
import co.sentinel.dvpn.domain.core.functional.Either
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn

/**
 * Abstract class for a UseCase that returns an instance of a [Flow].
 *
 * [T] represents the expected result wrapped in [Either]
 */
abstract class FlowableUseCase<out Type, in Params> where Type : Any {

    /**
     * Builds a [Flow] which will be used when the current [FlowableUseCase] is executed.
     */
    abstract suspend fun run(params: Params): Flow<Either<Failure, Type>>

    open operator fun invoke(
        scope: CoroutineScope,
        params: Params,
        onResult: (Either<Failure, Type>) -> Unit = {}
    ) {
        scope.launch(Dispatchers.Main) {
            val deferred = async(Dispatchers.IO) { run(params).flowOn(Dispatchers.Default) }
            deferred.await().collect { onResult(it) }
        }
    }
}

/**
 * Abstract class for a UseCase that returns an instance of a [Flow].
 *
 * [T] represents the expected result wrapped in [Either]
 *
 * Does not expect any params.
 */
abstract class FlowableUseCaseNoParams<T> {

    /**
     * Builds a [Flow] which will be used when the current [FlowableUseCase] is executed.
     */
    abstract suspend fun run(): Flow<Either<Failure, T>>

    open operator fun invoke(
        scope: CoroutineScope,
        onResult: (Either<Failure, T>) -> Unit = {}
    ) {
        scope.launch(Dispatchers.Main) {
            val deferred = async(Dispatchers.IO) { run().flowOn(Dispatchers.Default) }
            deferred.await().collect { onResult(it) }
        }
    }
}
