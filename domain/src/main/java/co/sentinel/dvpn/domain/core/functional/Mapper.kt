package co.sentinel.dvpn.domain.core.functional

/**
 * Interface for model mappers.
 *
 * @param <M> input type
 * @param <E> output type
 */
interface Mapper<in M, out E> {
    fun map(obj: M): E
}