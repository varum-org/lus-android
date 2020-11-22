package com.vtnd.lus.ui.main.container.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.vtnd.lus.base.BaseViewModel
import com.vtnd.lus.data.UserRepository
import com.vtnd.lus.data.model.User
import com.vtnd.lus.shared.scheduler.dispatcher.AppDispatchers
import com.vtnd.lus.shared.scheduler.dispatcher.DispatchersProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.koin.core.KoinComponent
import org.koin.core.get
import org.koin.core.qualifier.named

class ProfileViewModel(
    private val userRepository: UserRepository
) : BaseViewModel(), KoinComponent {
    private val dispatchersProvider =
        get<DispatchersProvider>(named(AppDispatchers.MAIN)).dispatcher()

    @ExperimentalCoroutinesApi
    val userProfile: LiveData<User?> = userRepository.userObservable()
        .map { it }
        .distinctUntilChanged()
        .flowOn(dispatchersProvider)
        .buffer(1)
        .asLiveData()
}
