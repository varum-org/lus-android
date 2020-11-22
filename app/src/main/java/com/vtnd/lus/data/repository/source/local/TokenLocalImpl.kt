package com.vtnd.lus.data.repository.source.local

import android.content.SharedPreferences
import com.vtnd.lus.data.repository.source.TokenDataSource
import com.vtnd.lus.data.repository.source.local.api.SharedPrefApi
import com.vtnd.lus.data.repository.source.local.api.pref.SharedPrefKey
import com.vtnd.lus.shared.scheduler.dispatcher.AppDispatchers
import com.vtnd.lus.shared.scheduler.dispatcher.DispatchersProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.koin.core.KoinComponent
import org.koin.core.get
import org.koin.core.qualifier.named
import timber.log.Timber


@ExperimentalCoroutinesApi
class TokenLocalImpl(
    private val sharedPrefApi: SharedPrefApi
) : TokenDataSource.Local, KoinComponent {
    private val dispatchersProvider =
        get<DispatchersProvider>(named(AppDispatchers.IO)).dispatcher()

    private var tokenObservable = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, k ->
            if (SharedPrefKey.KEY_TOKEN == k) {
                offer(sharedPrefApi.get(SharedPrefKey.KEY_TOKEN, String::class.java))
                Timber.d("change share preferences listener")
            }
        }
        send(sharedPrefApi.get(SharedPrefKey.KEY_TOKEN, String::class.java))
        sharedPrefApi.registerOnSharedPreferenceChangeListener(listener)
        awaitClose {
            Timber.d("close share preferences listener")
            sharedPrefApi.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }

    override fun getToken() =
        sharedPrefApi.get(SharedPrefKey.KEY_TOKEN, String::class.java)

    override fun saveToken(token: String) =
        sharedPrefApi.put(SharedPrefKey.KEY_TOKEN, token)

    override fun clearToken() = sharedPrefApi.removeKey(SharedPrefKey.KEY_TOKEN)

    override fun tokenObservable() =
        tokenObservable.flowOn(dispatchersProvider)
        .map { it }
        .buffer(1)
        .also { Timber.i("Token $it") }
}
