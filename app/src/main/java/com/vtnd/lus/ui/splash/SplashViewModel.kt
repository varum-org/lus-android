package com.vtnd.lus.ui.splash

import androidx.lifecycle.viewModelScope
import com.vtnd.lus.base.BaseViewModel
import com.vtnd.lus.data.RepoRepository
import com.vtnd.lus.data.TokenRepository
import com.vtnd.lus.data.UserRepository
import com.vtnd.lus.shared.liveData.SingleLiveData
import kotlinx.coroutines.launch

class SplashViewModel(
        private val repoRepository: RepoRepository,
        private val tokenRepository: TokenRepository,
        private val userRepository: UserRepository
) : BaseViewModel() {
    val isOpenFirstApp = repoRepository.isOpenFirstApp().isNullOrEmpty()
    val token = !tokenRepository.getToken().isNullOrBlank()
    val isSaved = SingleLiveData<Unit>()
    val isSavedService = SingleLiveData<Unit>()

    init {
        saveServiceToLocal()
    }

    fun saveUserToLocal() {
        viewModelScope.launch {
            userRepository.user()?.user?.id?.let {
                getUser()
            }
        }
    }

    private fun saveServiceToLocal() {
        viewModelScope(isSavedService,
                isShowLoading = false,
                onRequest = { repoRepository.getServices() },
                onSuccess = {
                    isSavedService.postValue(Unit)
                    showLoading(true)
                },
                onError = { exception.postValue(it) }
        )
    }

    private fun getUser() {
        viewModelScope(isSaved,
                isShowLoading = false,
                onRequest = { userRepository.getUser() },
                onSuccess = {
                    isSaved.postValue(Unit)
                    showLoading(true)
                },
                onError = { exception.postValue(it) }
        )
    }
}
