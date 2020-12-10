package com.vtnd.lus.ui.main.container.registerIdol

import android.Manifest
import androidx.annotation.RequiresPermission
import com.vtnd.lus.base.BaseViewModel
import com.vtnd.lus.data.RepoRepository
import com.vtnd.lus.shared.liveData.SingleLiveData
import com.vtnd.lus.ui.main.container.registerIdol.addressIdol.DomainLocation

class RegisterIdolViewModel(private val repoRepository: RepoRepository) : BaseViewModel() {
    val locationLiveData = SingleLiveData<DomainLocation>()
    val addressLiveData = SingleLiveData<String>()

    fun setLocation(domainLocation: DomainLocation, address: String?) {
        locationLiveData.postValue(domainLocation)
        address?.let {
            addressLiveData.postValue(it)
            return
        }
    }

    @RequiresPermission(anyOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    fun getCurrentLocation() {
        viewModelScope(
            locationLiveData,
            isShowLoading = false,
            onRequest = { repoRepository.getCurrentLocation() },
            onSuccess = { locationLiveData.postValue(it) },
            onError = { exception.postValue(it) })
    }
}
