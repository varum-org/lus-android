package com.vtnd.lus.ui.main.container.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.vtnd.lus.base.BaseViewModel
import com.vtnd.lus.base.ItemViewHolder
import com.vtnd.lus.data.UserRepository
import com.vtnd.lus.data.model.User
import com.vtnd.lus.shared.liveData.SingleLiveData
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
    val menuUserLiveData = SingleLiveData<List<ItemViewHolder<Any>>>()
    val menuIdolLiveData = SingleLiveData<List<ItemViewHolder<Any>>>()
    val menuSettingLiveData = SingleLiveData<List<ItemViewHolder<Any>>>()
    private val dispatchersProvider =
        get<DispatchersProvider>(named(AppDispatchers.MAIN)).dispatcher()

    @ExperimentalCoroutinesApi
    val userProfile: LiveData<User?> = userRepository.userObservable()
        .map { it }
        .distinctUntilChanged()
        .flowOn(dispatchersProvider)
        .buffer(1)
        .asLiveData()

    init {
        postMenuIdol()
        postMenuUser()
        postMenuSetting()
    }

    fun postMenuUser() {
        menuUserLiveData.postValue(
            listOf(
                Any(),
                Any(),
                Any(),
                Any(),
                Any(),
                Any(),
                Any(),
                Any(),
                Any(),
                Any()
            ).map { ItemViewHolder(it) }
        )
    }

    fun postMenuIdol() {
        menuIdolLiveData.postValue(listOf(Any()).map { ItemViewHolder(it) })
    }

    fun postMenuSetting() {
        menuSettingLiveData.postValue(
            listOf(
                Any(),
                Any(),
                Any(),
                Any(),
                Any()
            ).map { ItemViewHolder(it) })
    }
}
