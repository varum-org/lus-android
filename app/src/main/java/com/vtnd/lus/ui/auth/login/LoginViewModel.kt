package com.vtnd.lus.ui.auth.login

import com.vtnd.lus.base.BaseViewModel
import com.vtnd.lus.data.TokenRepository
import com.vtnd.lus.data.UserRepository
import com.vtnd.lus.data.repository.source.remote.api.response.SignInResponse
import com.vtnd.lus.shared.liveData.SingleLiveData

class LoginViewModel(
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository
) : BaseViewModel() {
    val signInResponse = SingleLiveData<SignInResponse>()

    fun signIn(email: String, password: String) {
        viewModelScope(signInResponse,
            onRequest = { userRepository.signIn(email, password) },
            onSuccess = {
                signInResponse.postValue(it).also { _ ->
                    tokenRepository.saveToken(it.token)
                }
            },
            onError = { exception.postValue(it) }
        )
    }
}
