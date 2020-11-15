package com.vtnd.lus.ui.main

import androidx.lifecycle.asLiveData
import com.vtnd.lus.base.BaseViewModel
import com.vtnd.lus.data.TokenRepository
import com.vtnd.lus.data.UserRepository
import com.vtnd.lus.shared.liveData.SingleLiveData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

class MainViewModel(
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository
) : BaseViewModel() {

    val isLogin = SingleLiveData<Boolean>()

    @ExperimentalCoroutinesApi
    val logoutEvent = tokenRepository
        .tokenObservable()
        .distinctUntilChanged()
        .filter { it.isNullOrEmpty() }
        .map { Unit }.asLiveData()

    init {
        isLogin()
    }

    private fun isLogin() {
        viewModelScope(null,
            isShowLoading = false,
            onRequest = { userRepository.isLogin() },
            onSuccess = { isLogin.postValue(!it.isNullOrEmpty()) },
            onError = { exception.postValue(it) }
        )
    }
}
