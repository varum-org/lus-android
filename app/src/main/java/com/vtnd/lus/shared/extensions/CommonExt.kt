package com.vtnd.lus.shared.extensions

/**
 * Returns a list containing all elements except last [n] elements.
 */
fun <E> List<E>.removeLast(n: Int): MutableList<E> {
    if (n > size) {
        throw IllegalArgumentException("Requested element $n is larger than list size $size")
    }
    if (n < 0) {
        throw IllegalArgumentException("Requested element $n is less than zero")
    }
    return dropLast(size - n).toMutableList()
}
