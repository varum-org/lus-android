package com.vtnd.lus.ui.main

import androidx.lifecycle.asLiveData
import com.vtnd.lus.base.BaseViewModel
import com.vtnd.lus.data.RepoRepository
import com.vtnd.lus.data.TokenRepository
import com.vtnd.lus.data.UserRepository
import com.vtnd.lus.shared.liveData.SingleLiveData
import com.vtnd.lus.shared.scheduler.dispatcher.AppDispatchers
import com.vtnd.lus.shared.scheduler.dispatcher.DispatchersProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
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

    @ExperimentalCoroutinesApi
    val userObservable = userRepository.userObservable()
        .map { it }
        .distinctUntilChanged()
        .flowOn(dispatchersProvider)
        .buffer(1)
        .asLiveData()

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
