package com.vtnd.lus.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vtnd.lus.base.BaseViewModel
import com.vtnd.lus.data.TokenRepository
import com.vtnd.lus.data.UserRepository
import com.vtnd.lus.shared.ValidateError
import com.vtnd.lus.shared.liveData.SingleLiveData
import com.vtnd.lus.shared.type.ValidateErrorType.EmailErrorType
import com.vtnd.lus.shared.type.ValidateErrorType.PasswordErrorType

class LoginViewModel(
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository
) : BaseViewModel() {
    val signInResponse = SingleLiveData<Any>()
    private val validateInput = ValidateError()
    private val _emailError = MutableLiveData<EmailErrorType>()
    private val _passwordError = MutableLiveData<PasswordErrorType>()
    val emailError: LiveData<EmailErrorType> get() = _emailError
    val passwordError: LiveData<PasswordErrorType> get() = _passwordError

    fun signIn(email: String?, password: String?) {
        val emailValidation = validateInput.validateEmail(email)
        val passwordValidation = validateInput.validatePassword(password)
        _emailError.value = emailValidation.second as EmailErrorType
        _passwordError.value = passwordValidation.second as PasswordErrorType
        if (emailValidation.first && passwordValidation.first) {
            viewModelScope(signInResponse,
                onRequest = { userRepository.signIn(email!!, password!!) },
                onSuccess = {
                    signInResponse.postValue(it)
                    showLoading(true)
                },
                onError = { exception.postValue(it) }
            )
        }
    }
}
