package com.vtnd.lus.ui.main.container.registerIdol

import android.Manifest
import androidx.annotation.RequiresPermission
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vtnd.lus.base.BaseViewModel
import com.vtnd.lus.data.RepoRepository
import com.vtnd.lus.data.model.Idol
import com.vtnd.lus.data.model.Service
import com.vtnd.lus.shared.ValidateError
import com.vtnd.lus.shared.liveData.SingleLiveData
import com.vtnd.lus.shared.scheduler.dispatcher.AppDispatchers
import com.vtnd.lus.shared.scheduler.dispatcher.DispatchersProvider
import com.vtnd.lus.shared.type.ValidateErrorType
import com.vtnd.lus.ui.main.container.registerIdol.addressIdol.DomainLocation
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.get
import org.koin.core.qualifier.named
import timber.log.Timber

class RegisterIdolViewModel(
    private val repoRepository: RepoRepository
) : BaseViewModel(), KoinComponent {
    private val dispatchersProvider =
        get<DispatchersProvider>(named(AppDispatchers.MAIN)).dispatcher()
    private val _nickNameError =
        MutableLiveData<ValidateErrorType.BaseErrorType>(ValidateErrorType.BaseErrorType.IS_EMPTY)
    private val _relationshipError =
        MutableLiveData<ValidateErrorType.BaseErrorType>(ValidateErrorType.BaseErrorType.IS_EMPTY)
    private val _descriptionError =
        MutableLiveData<ValidateErrorType.BaseErrorType>(ValidateErrorType.BaseErrorType.IS_EMPTY)
    private var nickNameText: String = ""
    private var relationshipText: String = ""
    private var descriptionText: String = ""
    private val validateInput = ValidateError()
    private var serviceRequests = mutableListOf<Service>()
    private var galleryRequests = mutableListOf<String>()
    private var idolRequest = Idol()
    val servicesLiveData = SingleLiveData<List<Service>>()
    val galleryLiveData = SingleLiveData<List<String>>()
    val locationLiveData = SingleLiveData<DomainLocation>()
    val addressLiveData = SingleLiveData<String>()
    val idolRequestLiveData = SingleLiveData<Idol>()
    val nickNameError: LiveData<ValidateErrorType.BaseErrorType> get() = _nickNameError
    val relationshipError: LiveData<ValidateErrorType.BaseErrorType> get() = _relationshipError
    val descriptionError: LiveData<ValidateErrorType.BaseErrorType> get() = _descriptionError


    init {
        viewModelScope.launch(dispatchersProvider) {
            repoRepository.services()?.let {
                servicesLiveData.postValue(it)
            }
            idolRequestLiveData.postValue(idolRequest)
            galleryLiveData.postValue(galleryRequests)
        }
    }

    fun postNickNameText(nickName: String) {
        idolRequestLiveData.postValue(idolRequest.copy(nickName = nickName))
        val nicknameValidation = validateInput.validateBase(nickName)
        _nickNameError.value = nicknameValidation.second as ValidateErrorType.BaseErrorType
    }

    fun postRelationshipText(relationship: String) {
        idolRequestLiveData.postValue(idolRequest.copy(relationship = relationship))
        val relationshipValidation = validateInput.validateBase(relationship)
        _relationshipError.value = relationshipValidation.second as ValidateErrorType.BaseErrorType
    }

    fun postDescriptionText(description: String) {
        idolRequestLiveData.postValue(idolRequest.copy(description = description))
        val descriptionValidation = validateInput.validateBase(description)
        _descriptionError.value = descriptionValidation.second as ValidateErrorType.BaseErrorType
    }

    fun check() {
        val descriptionValidation = validateInput.validateBase(descriptionText)
        val relationshipValidation = validateInput.validateBase(relationshipText)
        val nicknameValidation = validateInput.validateBase(nickNameText)
        Timber.i("${nicknameValidation.first}--${relationshipValidation.first}--${descriptionValidation.first}")
        if (nicknameValidation.first
            && relationshipValidation.first
            && descriptionValidation.first
        ) {

        }
    }
    fun postService(service: Service) {
        val oldService = serviceRequests.singleOrNull { it.serviceCode == service.serviceCode }
        serviceRequests = if (oldService == null) {
            serviceRequests.apply {
                add(service)
            }
        } else {
            serviceRequests.apply {
                set(indexOf(oldService), service)
            }
        }
        idolRequest = idolRequest.copy(services = serviceRequests)
        idolRequestLiveData.postValue(idolRequest)
    }

    fun deleteService(service: Service) {
        serviceRequests = serviceRequests.apply {
            remove(service)
        }
        idolRequest = idolRequest.copy(services = serviceRequests)
        idolRequestLiveData.postValue(idolRequest)
    }

    fun setLocation(domainLocation: DomainLocation, address: String?) {
        locationLiveData.postValue(domainLocation)
        address?.let {
            addressLiveData.postValue(it)
            return
        } ?: getAddressForCoordinates(domainLocation)
    }

    @RequiresPermission(anyOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    fun getCurrentLocation() {
        viewModelScope(
            locationLiveData,
            isShowLoading = false,
            onRequest = { repoRepository.getCurrentLocation() },
            onSuccess = {
                locationLiveData.postValue(it)
                getAddressForCoordinates(it)
            },
            onError = { exception.postValue(it) })
    }

    private fun getAddressForCoordinates(location: DomainLocation) {
        viewModelScope(
            addressLiveData,
            isShowLoading = false,
            onRequest = { repoRepository.getAddressForCoordinates(location) },
            onSuccess = { addressLiveData.postValue(it) },
            onError = { exception.postValue(it) })
    }
}
