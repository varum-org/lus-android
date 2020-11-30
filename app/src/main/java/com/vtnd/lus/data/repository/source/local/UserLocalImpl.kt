package com.vtnd.lus.data.repository.source.local

import android.content.SharedPreferences
import com.vtnd.lus.data.model.User
import com.vtnd.lus.data.model.UserJsonAdapter
import com.vtnd.lus.data.repository.source.UserDataSource
import com.vtnd.lus.data.repository.source.local.api.SharedPrefApi
import com.vtnd.lus.data.repository.source.local.api.pref.SharedPrefKey
import com.vtnd.lus.data.repository.source.local.api.pref.SharedPrefKey.KEY_LOGIN
import com.vtnd.lus.data.repository.source.local.api.pref.SharedPrefKey.VALUE_LOGIN
import com.vtnd.lus.shared.scheduler.dispatcher.AppDispatchers
import com.vtnd.lus.shared.scheduler.dispatcher.DispatchersProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import org.koin.core.get
import org.koin.core.qualifier.named
import timber.log.Timber

@ExperimentalCoroutinesApi
class UserLocalImpl(
//    private val databaseApi: DatabaseApi,
    private val sharedPrefApi: SharedPrefApi,
    private val userJsonAdapter: UserJsonAdapter
) : UserDataSource.Local, KoinComponent {
    private val dispatchersProvider =
        get<DispatchersProvider>(named(AppDispatchers.IO)).dispatcher()

    private var userObservable = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, k ->
            if (SharedPrefKey.KEY_USER == k) {
                offer(sharedPrefApi.get(SharedPrefKey.KEY_USER, String::class.java))
                Timber.d("change share preferences listener")
            }
        }
        send(sharedPrefApi.get(SharedPrefKey.KEY_USER, String::class.java))
        sharedPrefApi.registerOnSharedPreferenceChangeListener(listener)
        awaitClose {
            Timber.d("close share preferences listener")
            sharedPrefApi.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }

    private var loginObservable = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, k ->
            if (SharedPrefKey.KEY_USER == k) {
                offer(sharedPrefApi.get(KEY_LOGIN, String::class.java))
            }
        }
        send(sharedPrefApi.get(KEY_LOGIN, String::class.java))
        sharedPrefApi.registerOnSharedPreferenceChangeListener(listener)
        awaitClose {
            sharedPrefApi.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }

    override fun userObservable() =
        userObservable.flowOn(dispatchersProvider)
            .map { json -> json?.toUser() }
            .buffer(1)
            .also { Timber.i("User $it") }

    override suspend fun user() =
        withContext(dispatchersProvider) {
            sharedPrefApi.get(SharedPrefKey.KEY_USER, String::class.java).toUser()
        }

    override suspend fun saveUser(user: User) {
        withContext(dispatchersProvider) {
            userJsonAdapter.toJson(user).let {
                sharedPrefApi.put(SharedPrefKey.KEY_USER, it)
            }
        }
    }

    override suspend fun setLogin() =
        withContext(dispatchersProvider) {
            sharedPrefApi.put(KEY_LOGIN, VALUE_LOGIN)
        }

    override fun isLogin() =
        loginObservable.flowOn(dispatchersProvider)
            .map { it }
            .buffer(1)
            .also { Timber.i("Login $it") }

    override suspend fun clearLogin() =
        withContext(dispatchersProvider) {
            sharedPrefApi.removeKey(KEY_LOGIN)
        }

    private fun String?.toUser(): User? =
        runCatching { userJsonAdapter.fromJson(this ?: return null) }.getOrNull()
}
