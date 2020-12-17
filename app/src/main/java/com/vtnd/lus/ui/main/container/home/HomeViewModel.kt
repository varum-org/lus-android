package com.vtnd.lus.ui.main.container.home

import androidx.lifecycle.viewModelScope
import com.vtnd.lus.base.BaseViewModel
import com.vtnd.lus.base.ItemViewHolder
import com.vtnd.lus.data.TokenRepository
import com.vtnd.lus.data.UserRepository
import com.vtnd.lus.data.repository.source.remote.api.response.IdolResponse
import com.vtnd.lus.shared.liveData.SingleLiveData
import com.vtnd.lus.shared.scheduler.dispatcher.AppDispatchers
import com.vtnd.lus.shared.scheduler.dispatcher.DispatchersProvider
import com.vtnd.lus.shared.type.CategoryIdolType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.get
import org.koin.core.qualifier.named

class HomeViewModel(
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository
) : BaseViewModel(), KoinComponent {
    private val dispatchersProvider = get<DispatchersProvider>(named(AppDispatchers.MAIN)).dispatcher()
    val hotIdols = SingleLiveData<List<ItemViewHolder<IdolResponse>>>()
    val storyIdols = SingleLiveData<List<ItemViewHolder<IdolResponse>>>()
    val recommendIdols = SingleLiveData<List<ItemViewHolder<IdolResponse>>>()

    init {
        initialHotIdols()
    }

    @ExperimentalCoroutinesApi
    fun checkLogin(check: (Boolean) -> Unit) {
        viewModelScope.launch {
            check.invoke(!tokenRepository.getToken().isNullOrEmpty())
        }
    }

    fun initialHotIdols() {
        viewModelScope(
            null,
            onRequest = {
                userRepository.getIdols(
                    !tokenRepository.getToken().isNullOrEmpty(),
                    CategoryIdolType.RATING
                )
            },
            onSuccess = {
                val newIdols = it.map { idolResponse -> ItemViewHolder(idolResponse) }
                viewModelScope.launch(dispatchersProvider) {
                    recommendIdols.postValue(newIdols)
                    storyIdols.postValue(newIdols)
                    hotIdols.postValue(newIdols)
                }
            },
            onError = { exception.postValue(it) }
        )
    }
}
