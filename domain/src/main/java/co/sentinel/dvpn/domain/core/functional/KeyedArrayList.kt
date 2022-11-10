package co.sentinel.dvpn.domain.core.functional

/**
 * ArrayList that allows looking up elements by some key property. As the key property must always
 * be retrievable, this list cannot hold `null` elements. Because this class places no
 * restrictions on the order or duplication of keys, lookup by key, as well as all list modification
 * operations, require O(n) time.
 */
open class KeyedArrayList<K, E : Keyed<out K>> : ArrayList<E>() {
    fun containsKey(key: K) = indexOfKey(key) >= 0

    operator fun get(key: K): E? {
        val index = indexOfKey(key)
        return if (index >= 0) get(index) else null
    }

    open fun indexOfKey(key: K): Int {
        val iterator = listIterator()
        while (iterator.hasNext()) {
            val index = iterator.nextIndex()
            if (iterator.next().key == key)
                return index
        }
        return -1
    }
}