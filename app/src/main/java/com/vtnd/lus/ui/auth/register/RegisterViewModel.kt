package com.vtnd.lus.ui.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vtnd.lus.base.BaseViewModel
import com.vtnd.lus.data.UserRepository
import com.vtnd.lus.data.repository.source.remote.api.request.SignUpRequest
import com.vtnd.lus.data.repository.source.remote.api.response.SignInResponse
import com.vtnd.lus.shared.ValidateError
import com.vtnd.lus.shared.liveData.SingleLiveData
import com.vtnd.lus.shared.type.ValidateErrorType.*

class RegisterViewModel(private val userRepository: UserRepository) : BaseViewModel() {
    val signUpResponse = SingleLiveData<Unit>()
    private val validateInput = ValidateError()
    private val _emailError = MutableLiveData<EmailErrorType>()
    private val _passwordError = MutableLiveData<PasswordErrorType>()
    private val _userNameError = MutableLiveData<UserNameErrorType>()
    private val _phoneError = MutableLiveData<PhoneErrorType>()
    val emailError: LiveData<EmailErrorType> get() = _emailError
    val passwordError: LiveData<PasswordErrorType> get() = _passwordError
    val userNameError: LiveData<UserNameErrorType> get() = _userNameError
    val phoneError: LiveData<PhoneErrorType> get() = _phoneError

    fun signUp(signUpRequest: SignUpRequest) {
        val emailValidation = validateInput.validateEmail(signUpRequest.email)
        val passwordValidation = validateInput.validatePassword(signUpRequest.password)
        val userNameValidation = validateInput.validateUserName(signUpRequest.userName)
        val phoneValidation = validateInput.validatePhone(signUpRequest.phone)
        _emailError.value = emailValidation.second as EmailErrorType
        _passwordError.value = passwordValidation.second as PasswordErrorType
        _userNameError.value = userNameValidation.second as UserNameErrorType
        _phoneError.value = phoneValidation.second as PhoneErrorType
        if (emailValidation.first
            && passwordValidation.first
            && userNameValidation.first
            && phoneValidation.first
        ) {
            viewModelScope(signUpResponse,
                onRequest = { userRepository.signUp(signUpRequest) },
                onSuccess = {
                    signUpResponse.postValue(it)
                },
                onError = { exception.postValue(it) }
            )
        }
    }
}
