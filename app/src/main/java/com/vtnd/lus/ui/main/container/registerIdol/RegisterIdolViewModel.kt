package com.vtnd.lus.ui.main.container.registerIdol

import android.Manifest
import android.net.Uri
import androidx.annotation.RequiresPermission
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vtnd.lus.base.BaseViewModel
import com.vtnd.lus.data.RepoRepository
import com.vtnd.lus.data.UserRepository
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

class RegisterIdolViewModel(
    private val repoRepository: RepoRepository,
    private val userRepository: UserRepository
) : BaseViewModel(), KoinComponent {
    private val dispatchersProvider =
        get<DispatchersProvider>(named(AppDispatchers.MAIN)).dispatcher()
    private val _nickNameError =
        MutableLiveData<ValidateErrorType.BaseErrorType>()
    private val _relationshipError =
        MutableLiveData<ValidateErrorType.BaseErrorType>()
    private val _descriptionError =
        MutableLiveData<ValidateErrorType.BaseErrorType>()
    private val _urisError =
        MutableLiveData<ValidateErrorType.BaseErrorType>()
    private val validateInput = ValidateError()
    private var serviceRequests = mutableListOf<Service>()
    private var galleryRequests = mutableListOf<Uri>()
    private var idolRequest = Idol()
    val servicesLiveData = SingleLiveData<List<Service>>()
    val galleryLiveData = SingleLiveData<List<Uri>>()
    val locationLiveData = SingleLiveData<DomainLocation>()
    val addressLiveData = SingleLiveData<String>()
    val idolRequestLiveData = SingleLiveData<Idol>()
    val nickNameError: LiveData<ValidateErrorType.BaseErrorType> get() = _nickNameError
    val relationshipError: LiveData<ValidateErrorType.BaseErrorType> get() = _relationshipError
    val descriptionError: LiveData<ValidateErrorType.BaseErrorType> get() = _descriptionError
    val urisError: LiveData<ValidateErrorType.BaseErrorType> get() = _urisError
    val isShowButton = SingleLiveData<Boolean>().apply { postValue(false) }
    val isShowButtonRegister = SingleLiveData<Boolean>().apply { postValue(false) }
    val idolResponse =SingleLiveData<Unit>()

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
        val nicknameValidation = validateInput.validateBase(nickName)
        idolRequest = idolRequest.copy(nickName = nickName)
        idolRequestLiveData.postValue(idolRequest)
        _nickNameError.value = nicknameValidation.second as ValidateErrorType.BaseErrorType
        checkInformation()
    }

    fun postRelationshipText(relationship: String) {
        val relationshipValidation = validateInput.validateBase(relationship)
        idolRequest = idolRequest.copy(relationship = relationship)
        idolRequestLiveData.postValue(idolRequest)
        _relationshipError.value = relationshipValidation.second as ValidateErrorType.BaseErrorType
        checkInformation()
    }

    fun postDescriptionText(description: String) {
        val descriptionValidation = validateInput.validateBase(description)
        idolRequest = idolRequest.copy(description = description)
        idolRequestLiveData.postValue(idolRequest)
        _descriptionError.value = descriptionValidation.second as ValidateErrorType.BaseErrorType
        checkInformation()
    }

    fun checkInformation() {
        val relationshipValidation = validateInput.validateBase(idolRequest.relationship)
        val nicknameValidation = validateInput.validateBase(idolRequest.nickName)
        val descriptionValidation = validateInput.validateBase(idolRequest.description)
        if (nicknameValidation.first
            && relationshipValidation.first
            && descriptionValidation.first
        ) isShowButton.postValue(true)
        else isShowButton.postValue(false)
        checkRegisterIdol()
    }

    fun checkRegisterIdol() {
        val relationshipValidation = validateInput.validateBase(idolRequest.relationship)
        val nicknameValidation = validateInput.validateBase(idolRequest.nickName)
        val descriptionValidation = validateInput.validateBase(idolRequest.description)
        val serviceValidation = validateInput.validateServicesEmpty(serviceRequests)
        val urisValidation = validateInput.validateUrisEmpty(galleryRequests)
        if (nicknameValidation.first
            && relationshipValidation.first
            && descriptionValidation.first
            && serviceValidation.first
            && urisValidation.first
        ) {
            isShowButtonRegister.postValue(true)
        } else {
            isShowButtonRegister.postValue(false)
        }
    }

    fun checkServices() {
        val serviceValidation = validateInput.validateServicesEmpty(serviceRequests)
        if (serviceValidation.first
        ) isShowButton.postValue(true)
        else isShowButton.postValue(false)
        checkRegisterIdol()
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
        checkServices()
    }

    fun deleteService(service: Service) {
        serviceRequests = serviceRequests.apply {
            remove(service)
        }
        idolRequest = idolRequest.copy(services = serviceRequests)
        idolRequestLiveData.postValue(idolRequest)
        checkServices()
    }

    fun postImage(uri: Uri) {
        val oldImage = galleryRequests.singleOrNull { it == uri }
        galleryRequests = if (oldImage == null) {
            galleryRequests.apply {
                add(uri)
            }
        } else {
            galleryRequests.apply {
                set(indexOf(oldImage), uri)
            }
        }
        val urisValidation = validateInput.validateUrisEmpty(galleryRequests)
        _urisError.value = urisValidation.second as ValidateErrorType.BaseErrorType
        galleryLiveData.postValue(galleryRequests)
        checkRegisterIdol()
    }

    fun deleteImage(uri: Uri) {
        galleryRequests = galleryRequests.apply {
            remove(uri)
        }
        val urisValidation = validateInput.validateUrisEmpty(galleryRequests)
        _urisError.value = urisValidation.second as ValidateErrorType.BaseErrorType
        galleryLiveData.postValue(galleryRequests)
        checkRegisterIdol()
    }

    fun setLocation(domainLocation: DomainLocation) {
        locationLiveData.postValue(domainLocation)
        domainLocation.name?.let {
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
            onSuccess = {
                addressLiveData.postValue(it)
                locationLiveData.postValue(locationLiveData.value?.copy(name = it))
            },
            onError = { exception.postValue(it) })
    }
    fun registerIdol(){
        viewModelScope(
            null,
            onRequest = {
                userRepository.registerIdol(
                    idolRequest.copy(location = locationLiveData.value),
                    galleryRequests
                )
            },
            onSuccess = { idolResponse.postValue(Unit) },
            onError = { exception.postValue(it) }
        )
    }
}
