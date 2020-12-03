package com.vtnd.lus.ui.main

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.vtnd.lus.base.BaseViewModel
import com.vtnd.lus.data.RepoRepository
import com.vtnd.lus.data.TokenRepository
import com.vtnd.lus.data.UserRepository
import com.vtnd.lus.data.model.User
import com.vtnd.lus.shared.liveData.SingleLiveData
import com.vtnd.lus.shared.scheduler.dispatcher.AppDispatchers
import com.vtnd.lus.shared.scheduler.dispatcher.DispatchersProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.get
import org.koin.core.qualifier.named

class MainViewModel(
    userRepository: UserRepository,
    tokenRepository: TokenRepository,
    repoRepository: RepoRepository
) : BaseViewModel(), KoinComponent {
    private val dispatchersProvider =
        get<DispatchersProvider>(named(AppDispatchers.MAIN)).dispatcher()

    val user = SingleLiveData<User>()

    init {
        viewModelScope.launch(dispatchersProvider) {
            user.postValue(userRepository.user())
        }
    }

    @ExperimentalCoroutinesApi
    val logoutEvent = tokenRepository
        .tokenObservable()
        .distinctUntilChanged()
        .filter { it.isNullOrEmpty() }
        .map { Unit }.asLiveData()

    init {
        if (repoRepository.isOpenFirstApp().isNullOrEmpty())
            repoRepository.setOpenFirstApp()
    }
}
