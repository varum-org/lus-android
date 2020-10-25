package com.vtnd.lus.shared.extensions

import androidx.lifecycle.MutableLiveData

inline fun <T : Any> T?.notNull(f: (it: T) -> Unit) {
    if (this != null) f(this)
}

inline fun <T : Any> T?.isNull(f: () -> Unit) {
    if (this == null) f()
}

inline fun <T> List<T>.indexOf(notFoundIndex: Int = -1, predicate: (T) -> Boolean): Int {
    for ((index, item) in this.withIndex()) {
        if (predicate(item))
            return index
    }
    return notFoundIndex
}

inline fun <T> List<T>.firstItem(f: (it: T) -> Unit) {
    if (this.isNotEmpty()) f(this[0])
}

val <T> List<T>.lastIndex: Int
    get() = size - 1

fun MutableList<Any>.swap(index1: Int, index2: Int) {

    val tmp = this[index1]

    this[index1] = this[index2]

    this[index2] = tmp
}

operator fun <T> MutableLiveData<MutableList<T>>.plusAssign(values: List<T>) {
    val value = this.value ?: arrayListOf()
    value.addAll(values)
    this.value = value
}

fun <T : Any> checkNull(
    element: T?,
    ifNull: (() -> Unit)? = null,
    ifNotNull: ((T) -> Unit)? = null
) = if (element == null) ifNull?.invoke() else ifNotNull?.invoke(element)
