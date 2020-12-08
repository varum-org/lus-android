package com.vtnd.lus.data.repository.source.local

import android.content.SharedPreferences
import com.vtnd.lus.data.repository.source.UserDataSource
import com.vtnd.lus.data.repository.source.local.api.SharedPrefApi
import com.vtnd.lus.data.repository.source.local.api.pref.SharedPrefKey.KEY_USER
import com.vtnd.lus.data.repository.source.remote.api.response.IdolResponse
import com.vtnd.lus.data.repository.source.remote.api.response.IdolResponseJsonAdapter
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
    private val userJsonAdapter: IdolResponseJsonAdapter
) : UserDataSource.Local, KoinComponent {
    private val dispatchersProvider =
        get<DispatchersProvider>(named(AppDispatchers.IO)).dispatcher()

    private var userObservable = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, k ->
            if (KEY_USER == k) {
                offer(sharedPrefApi.get(KEY_USER, String::class.java))
                Timber.d("change share preferences listener")
            }
        }
        send(sharedPrefApi.get(KEY_USER, String::class.java))
        sharedPrefApi.registerOnSharedPreferenceChangeListener(listener)
        awaitClose {
            Timber.d("close share preferences listener")
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
            sharedPrefApi.get(KEY_USER, String::class.java).toUser()
        }

    override suspend fun saveUser(user: IdolResponse) {
        withContext(dispatchersProvider) {
            userJsonAdapter.toJson(user).let {
                sharedPrefApi.put(KEY_USER, it)
            }
        }
    }

    override suspend fun clearUser() {
        withContext(dispatchersProvider) {
            sharedPrefApi.removeKey(KEY_USER)
        }
    }

    private fun String?.toUser(): IdolResponse? =
        runCatching { userJsonAdapter.fromJson(this ?: return null) }.getOrNull()
}
