package com.vtnd.lus.data.repository.source.local.api.pref

import android.content.Context
import android.content.SharedPreferences
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.vtnd.lus.data.repository.source.local.api.SharedPrefApi

class SharedPrefApiImpl(context: Context, private val moshi: Moshi) : SharedPrefApi {

    private val sharedPreferences by lazy {
        context.getSharedPreferences(SharedPrefKey.PREFS_NAME, Context.MODE_PRIVATE)
    }

    override fun registerOnSharedPreferenceChangeListener(
        listener: SharedPreferences.OnSharedPreferenceChangeListener
    ) {
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun unregisterOnSharedPreferenceChangeListener(
        listener: SharedPreferences.OnSharedPreferenceChangeListener
    ) {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
    }

    override fun <T> put(key: String, data: T) {
        val editor = sharedPreferences.edit()
        when (data) {
            is String -> editor.putString(key, data)
            is Boolean -> editor.putBoolean(key, data)
            is Float -> editor.putFloat(key, data)
            is Int -> editor.putInt(key, data)
            is Long -> editor.putLong(key, data)
            is Set<*> -> editor.putStringSet(key, data.filterIsInstanceTo(mutableSetOf()))
            else -> {
                error("Not support for type clazz")
            }
        }
        editor.apply()
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> get(key: String, type: Class<T>, default: T?): T? = when (type) {
        String::class.java -> sharedPreferences.getString(key, default as? String) as? T
        Boolean::class.java -> java.lang.Boolean.valueOf(
            sharedPreferences.getBoolean(key, default as? Boolean ?: false)
        ) as? T
        Float::class.java -> java.lang.Float.valueOf(
            sharedPreferences.getFloat(key, default as? Float ?: 0f)
        ) as? T
        Int::class.java -> Integer.valueOf(
            sharedPreferences.getInt(key, default as? Int ?: 0)
        ) as? T
        Long::class.java -> java.lang.Long.valueOf(
            sharedPreferences.getLong(key, default as? Long ?: 0L)
        ) as? T
        Set::class.java ->
            sharedPreferences.getStringSet(key, default as Set<String>) as? T
        else -> {
            error("Not support for type clazz")
        }
    }

    override fun <T> putList(key: String, clazz: Class<T>, list: List<T>) {
        val listMyData = Types.newParameterizedType(MutableList::class.java, clazz)
        val adapter: JsonAdapter<List<T>> = moshi.adapter(listMyData)
        sharedPreferences.edit().putString(key, adapter.toJson(list)).apply()
    }

    override fun <T> getList(key: String, clazz: Class<T>): List<T>? {
        val listMyData = Types.newParameterizedType(MutableList::class.java, clazz)
        val adapter: JsonAdapter<List<T>> = moshi.adapter(listMyData)
        return get(key, String::class.java)?.let { adapter.fromJson(it) }
    }

    override fun removeKey(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }

    override fun clear() {
        sharedPreferences.edit().clear().apply()
    }
}
