package com.vtnd.lus.ui.auth.verify

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vtnd.lus.base.BaseViewModel
import com.vtnd.lus.data.UserRepository
import com.vtnd.lus.data.repository.source.remote.api.request.VerifyRequest
import com.vtnd.lus.shared.ValidateError
import com.vtnd.lus.shared.liveData.SingleLiveData
import com.vtnd.lus.shared.type.ValidateErrorType

class VerifyViewModel(private val userRepository: UserRepository) : BaseViewModel() {
    val verifyResponse = SingleLiveData<Any>()
    private val validateInput = ValidateError()
    private val _emailError = MutableLiveData<ValidateErrorType.EmailErrorType>()
    private val _codeError = MutableLiveData<ValidateErrorType.CodeErrorType>()

    val emailError: LiveData<ValidateErrorType.EmailErrorType> get() = _emailError
    val codeError: LiveData<ValidateErrorType.CodeErrorType> get() = _codeError

    fun verifyAccount(verifyRequest: VerifyRequest) {
        val emailValidation = validateInput.validateEmail(verifyRequest.email)
        val codeValidation = validateInput.validateCode(verifyRequest.emailCode)
        _emailError.value = emailValidation.second as ValidateErrorType.EmailErrorType
        _codeError.value = codeValidation.second as ValidateErrorType.CodeErrorType
        if (emailValidation.first && codeValidation.first) {
            viewModelScope(verifyResponse,
                onRequest = { userRepository.verifyAccount(verifyRequest) },
                onSuccess = { verifyResponse.postValue(it) },
                onError = { exception.postValue(it) }
            )
        }
    }
}
