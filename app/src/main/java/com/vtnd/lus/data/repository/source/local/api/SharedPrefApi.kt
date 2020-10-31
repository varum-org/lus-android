package com.vtnd.lus.data.repository.source.local.api

import android.content.SharedPreferences

interface SharedPrefApi {
    fun <T> put(key: String, data: T)
    fun <T> get(key: String, type: Class<T>, default: T? = null): T?
    fun removeKey(key: String)
    fun clear()

    fun registerOnSharedPreferenceChangeListener(
        listener: SharedPreferences.OnSharedPreferenceChangeListener
    )

    fun unregisterOnSharedPreferenceChangeListener(
        listener: SharedPreferences.OnSharedPreferenceChangeListener
    )
}
