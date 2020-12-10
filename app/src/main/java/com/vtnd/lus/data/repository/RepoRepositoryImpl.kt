package com.vtnd.lus.data.repository

import android.Manifest
import android.app.Application
import androidx.annotation.RequiresPermission
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.vtnd.lus.base.BaseRepository
import com.vtnd.lus.data.RepoRepository
import com.vtnd.lus.data.maps.toLocationDomain
import com.vtnd.lus.data.repository.source.RepoDataSource
import com.vtnd.lus.data.repository.source.remote.api.response.GeocoderErrorResponseJsonAdapter
import com.vtnd.lus.shared.extensions.retrySuspend
import com.vtnd.lus.shared.scheduler.dispatcher.AppDispatchers
import com.vtnd.lus.shared.scheduler.dispatcher.DispatchersProvider
import com.vtnd.lus.ui.main.container.registerIdol.addressIdol.DomainLocation
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import org.koin.core.KoinComponent
import org.koin.core.get
import org.koin.core.qualifier.named
import timber.log.Timber
import kotlin.time.ExperimentalTime
import kotlin.time.milliseconds
import kotlin.time.seconds

@ExperimentalCoroutinesApi
class RepoRepositoryImpl(
    private val remote: RepoDataSource.Remote,
    private val local: RepoDataSource.Local,
    private val application: Application,
    private val geocoderApiKey: String,
    private val geocoderErrorResponseJsonAdapter: GeocoderErrorResponseJsonAdapter
) : BaseRepository(), RepoRepository, KoinComponent {

    private val dispatchersProvider =
        get<DispatchersProvider>(named(AppDispatchers.MAIN)).dispatcher()
    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(application)
    }
    private val settingsClient by lazy {
        LocationServices.getSettingsClient(application)
    }
    private val locationRequest by lazy {
        LocationRequest()
            .setInterval(1_000)
            .setFastestInterval(500)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setNumUpdates(1)
    }
    private val locationSettingsRequest by lazy {
        LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .build()
    }

    override fun isOpenFirstApp() =
        local.isOpenFirstApp()

    override fun setOpenFirstApp() =
        local.setOpenFirstApp()

    override suspend fun services() = local.services()

    override fun servicesObservable() = local.servicesObservable()
        .distinctUntilChanged()
        .buffer(1).let {
            Timber.i("userObservable")
            it
        }

    @ExperimentalTime
    @RequiresPermission(anyOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override suspend fun getCurrentLocation() =
        withResultContext(dispatchersProvider) {
            try {
                settingsClient.checkLocationSettings(locationSettingsRequest).await()
            } catch (e: ResolvableApiException) {
                throw e
            }
            fusedLocationClient
                .lastLocation
                .await()
                .also { Timber.d("lastLocation: $it") }
                ?.run { return@withResultContext toLocationDomain() }
            try {
                retrySuspend(
                    times = 3,
                    initialDelay = 500.milliseconds,
                    factor = 2.0
                ) {
                    withTimeout(5.seconds) {
                        Timber.d("[requestLocationUpdates]: started times: $it")
                        requestLocationUpdates()
                    }
                }.toLocationDomain()
            } catch (e: TimeoutCancellationException) {
                throw e
            }
        }

    @RequiresPermission(anyOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    private suspend fun requestLocationUpdates(): android.location.Location {
        return suspendCancellableCoroutine { continuation ->
            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    (locationResult?.lastLocation ?: return)
                        .also { Timber.d("[locationCallback] resume: $it") }
                        .let {
                            continuation.resume(it) {
                                fusedLocationClient.removeLocationUpdates(this)
                                Timber.d("[locationCallback] remove")
                            }
                        }
                }
            }

            continuation.invokeOnCancellation {
                fusedLocationClient.removeLocationUpdates(locationCallback)
                Timber.d("[invokeOnCancellation]")
            }
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                null
            )
        }
    }

    override suspend fun getServices() = withResultContext {
        val services = remote.getServices().data
        local.saveServices(services ?: emptyList())
    }

    override suspend fun getAddressForCoordinates(location: DomainLocation) =
        withResultContext {
            val response = remote
                .getAddressForCoordinates(
                    latlng = "${location.latitude},${location.longitude}",
                    key = geocoderApiKey
                )

            if (!response.isSuccessful) {
                response
                    .errorBody()!!
                    .use { geocoderErrorResponseJsonAdapter.fromJson(it.toString())!! }
                    .let {
                        throw Throwable(it.errorMessage)
                    }
            }

            (response
                .body()!!
                .results
                .firstOrNull() ?: throw Throwable("Cannot get address from coordinates"))
                .formattedAddress
        }
}
