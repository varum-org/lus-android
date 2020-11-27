package com.vtnd.lus.data.repository.source.local

import android.content.SharedPreferences
import com.vtnd.lus.data.model.Service
import com.vtnd.lus.data.model.ServiceJsonAdapter
import com.vtnd.lus.data.repository.source.RepoDataSource
import com.vtnd.lus.data.repository.source.local.api.SharedPrefApi
import com.vtnd.lus.data.repository.source.local.api.pref.SharedPrefKey.KEY_OPEN_FIRST_APP
import com.vtnd.lus.data.repository.source.local.api.pref.SharedPrefKey.KEY_SERVICES
import com.vtnd.lus.data.repository.source.local.api.pref.SharedPrefKey.VALUE_OPEN_FIRST_APP
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

class RepoLocalImpl(
//    private val databaseApi: DatabaseApi,
    private val sharedPrefApi: SharedPrefApi,
    private val serviceJsonAdapter: ServiceJsonAdapter
) : RepoDataSource.Local, KoinComponent {
    private val dispatchersProvider =
        get<DispatchersProvider>(named(AppDispatchers.IO)).dispatcher()

    @ExperimentalCoroutinesApi
    private var servicesObservable = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, k ->
            if (KEY_SERVICES == k) {
                offer(sharedPrefApi.getList(KEY_SERVICES, Service::class.java))
                Timber.d("change share preferences listener")
            }
        }
        send(sharedPrefApi.getList(KEY_SERVICES, Service::class.java))
        sharedPrefApi.registerOnSharedPreferenceChangeListener(listener)
        awaitClose {
            Timber.d("close share preferences listener")
            sharedPrefApi.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }

    override fun isOpenFirstApp() =
        sharedPrefApi.get(KEY_OPEN_FIRST_APP, String::class.java)

    override fun setOpenFirstApp() =
        sharedPrefApi.put(KEY_OPEN_FIRST_APP, VALUE_OPEN_FIRST_APP)

    override suspend fun saveServices(services: List<Service>) =
        sharedPrefApi.putList(KEY_SERVICES, Service::class.java, services)

    override suspend fun services() = withContext(dispatchersProvider) {
        sharedPrefApi.getList(KEY_SERVICES, Service::class.java)
    }

    @ExperimentalCoroutinesApi
    override fun servicesObservable() = servicesObservable
        .flowOn(dispatchersProvider)
        .map { json -> json }
        .buffer(1)
        .also { Timber.i("Services $it") }

//    private fun String?.toServices() =
//        runCatching {
//            serviceJsonAdapter.fromJson(this ?: return null)
//        }.getOrNull()
}
