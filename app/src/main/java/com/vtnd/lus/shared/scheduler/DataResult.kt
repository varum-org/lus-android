package com.vtnd.lus.shared.scheduler

import androidx.lifecycle.LiveData

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class DataResult<out R> {

    data class Success<out T>(val data: T) : DataResult<T>()
    data class Error(val exception: Exception) : DataResult<Nothing>()
    object Loading : DataResult<Nothing>()

    inline fun executeIfSucceed(block: (data: R) -> Unit): DataResult<R> {
        if (this is Success<R>) block(this.data)
        return this
    }

    inline fun executeIfFailed(block: (ex: Exception) -> Unit): DataResult<R> {
        if (this is Error) block(this.exception)
        return this
    }

    inline fun <M> map(block: (R) -> M): DataResult<M> {
        return when (this) {
            is Success -> Success(block(data))
            is Error -> Error(exception)
            is Loading -> Loading
        }
    }

    inline fun <M> mapWithoutResult(success: (R) -> M): M? {
        return when (this) {
            is Success -> success(data)
            else -> null // Ignore loading and failed for synchronize code
        }
    }

    /**
     * Get data of result by status
     * If succeeded return the data
     * else return null
     */
    fun getResultData(): R? = when (this) {
        is Success -> data
        else -> null // Ignore loading and failed for synchronize code
    }

    fun isCompleted() = this is Success || this is Error

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            Loading -> "Loading"
        }
    }
}

/**
 * `true` if [DataResult] is of type [Success] & holds non-null [Success.data].
 */
val DataResult<*>.succeeded get() = this is DataResult.Success && data != null

val <T> LiveData<DataResult<T?>>.dataOfResult: T? get() = if (value is DataResult.Success<T?>) (value as DataResult.Success<T?>).data else null

/**
 * A observable list items include success case and error case
 */
typealias LiveResultItems<T> = LiveData<DataResult<List<T>>>

/**
 * A observable a item include success case and error case
 */
typealias LiveResult<T> = LiveData<DataResult<T>>
