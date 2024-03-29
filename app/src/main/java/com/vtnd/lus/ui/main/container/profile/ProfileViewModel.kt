package com.vtnd.lus.ui.main.container.profile

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.vtnd.lus.R
import com.vtnd.lus.base.BaseViewModel
import com.vtnd.lus.base.ItemViewHolder
import com.vtnd.lus.data.TokenRepository
import com.vtnd.lus.data.UserRepository
import com.vtnd.lus.shared.TYPE_ITEM
import com.vtnd.lus.shared.liveData.SingleLiveData
import com.vtnd.lus.shared.scheduler.DataResult
import com.vtnd.lus.shared.scheduler.dispatcher.AppDispatchers
import com.vtnd.lus.shared.scheduler.dispatcher.DispatchersProvider
import com.vtnd.lus.ui.main.container.profile.adapter.ItemMenu
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.get
import org.koin.core.qualifier.named

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository
) : BaseViewModel(), KoinComponent {
    val menuUserLiveData = SingleLiveData<List<ItemViewHolder<ItemMenu>>>()
    val menuIdolLiveData = SingleLiveData<List<ItemViewHolder<Any>>>()
    val menuSettingLiveData = SingleLiveData<List<ItemViewHolder<ItemMenu>>>()
    val logoutLiveData = SingleLiveData<Any>()
    private val dispatchersProvider =
        get<DispatchersProvider>(named(AppDispatchers.MAIN)).dispatcher()

    @ExperimentalCoroutinesApi
    val userProfile = userRepository.userObservable()
        .map { it }
        .distinctUntilChanged()
        .flowOn(dispatchersProvider)
        .buffer(1)
        .asLiveData()

    init {
        postMenuUser()
        postMenuSetting()
    }

    private fun postMenuUser() {
        menuUserLiveData.postValue(
            listOf(
                ItemMenu(
                    icon = R.drawable.ic_wallet,
                    title = R.string.wallet
                ),
                ItemMenu(
                    icon = R.drawable.ic_image,
                    title = R.string.gallery
                ),
                ItemMenu(
                    icon = R.drawable.ic_bill,
                    title = R.string.bill
                ),
                ItemMenu(
                    icon = R.drawable.ic_time_square,
                    title = R.string.history_idol
                ),
                ItemMenu(
                    icon = R.drawable.ic_add_user,
                    title = R.string.invite_friends
                ),
                ItemMenu(
                    icon = R.drawable.ic_message,
                    title = R.string.feedback
                )
            ).map { ItemViewHolder(it) }
        )
    }

    fun postMenuIdol(itemData: Any, type: Int = TYPE_ITEM) {
        viewModelScope.launch {
            menuIdolLiveData.postValue(listOf(ItemViewHolder(itemData).copy(type = type)))
        }
    }

    private fun postMenuSetting() {
        menuSettingLiveData.postValue(
            listOf(
                ItemMenu(
                    icon = R.drawable.ic_time_square,
                    title = R.string.history
                ),
                ItemMenu(
                    icon = R.drawable.ic_help,
                    title = R.string.help_and_support
                ),
                ItemMenu(
                    icon = R.drawable.ic_lock,
                    title = R.string.change_password
                ),
                ItemMenu(
                    icon = R.drawable.ic_logout,
                    title = R.string.log_out
                )
            ).map { ItemViewHolder(it) })
    }

    fun logout() {
        viewModelScope(logoutLiveData,
            onRequest = { userRepository.logout() },
            onError = { exception.postValue(it) },
            onSuccess = { logoutLiveData.postValue(it) })
    }

    fun getUser(success: () -> Unit) {
        viewModelScope(null,
            isShowLoading = true,
            onRequest = {
                userRepository.user()?.user?.let {
                    userRepository.getUser()
                } ?: DataResult.Success(Any())
            },
            onSuccess = {
                success.invoke()
            },
            onError = {
                success.invoke()
                exception.postValue(it)
            }
        )
    }

    @ExperimentalCoroutinesApi
    fun checkLogin(check: (Boolean) -> Unit) {
        viewModelScope.launch {
            check.invoke(!tokenRepository.getToken().isNullOrEmpty())
        }
    }
}
