package co.sentinel.dvpn.domain.core.functional

/**
 * Interface for objects that have a identifying key of the given type.
 */
interface Keyed<K> {
    val key: K
}