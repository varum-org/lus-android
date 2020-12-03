package com.vtnd.lus.ui.main.container.home

import androidx.lifecycle.viewModelScope
import com.vtnd.lus.base.BaseViewModel
import com.vtnd.lus.base.ItemViewHolder
import com.vtnd.lus.data.TokenRepository
import com.vtnd.lus.data.UserRepository
import com.vtnd.lus.data.model.Room
import com.vtnd.lus.data.repository.source.remote.api.request.RoomRequest
import com.vtnd.lus.data.repository.source.remote.api.response.IdolResponse
import com.vtnd.lus.shared.TYPE_HEADER
import com.vtnd.lus.shared.liveData.SingleLiveData
import com.vtnd.lus.shared.scheduler.dispatcher.AppDispatchers
import com.vtnd.lus.shared.scheduler.dispatcher.DispatchersProvider
import com.vtnd.lus.shared.type.CategoryIdolType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.get
import org.koin.core.qualifier.named

class HomeViewModel(
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository
) : BaseViewModel(), KoinComponent {
    private val dispatchersProvider =
        get<DispatchersProvider>(named(AppDispatchers.MAIN)).dispatcher()
    val hotIdols = SingleLiveData<List<IdolResponse>>()
    val storyIdols = SingleLiveData<List<ItemViewHolder<Any>>>()

    init {
        initialStoryIdols()
        initialHotIdols()
    }

    @ExperimentalCoroutinesApi
    fun checkLogin(check: (Boolean) -> Unit) {
        viewModelScope.launch {
            check.invoke(!tokenRepository.getToken().isNullOrEmpty())
        }
    }

    private fun initialStoryIdols() {
        mutableListOf<ItemViewHolder<Any>>().apply {
            add(ItemViewHolder(type = TYPE_HEADER, itemData = Any()))
            for (i in 2..10) add(ItemViewHolder(Any()))
        }.let(storyIdols::postValue)
    }

    private fun initialHotIdols() {
        viewModelScope(
            hotIdols,
            onRequest = {
                userRepository.getIdols(
                    !tokenRepository.getToken().isNullOrEmpty(),
                    CategoryIdolType.RATING
                )
            },
            onSuccess = { hotIdols.postValue(it) },
            onError = { exception.postValue(it) }
        )
    }
}
