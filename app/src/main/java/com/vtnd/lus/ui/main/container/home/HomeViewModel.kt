package com.vtnd.lus.ui.main.container.home

import com.vtnd.lus.base.BaseViewModel
import com.vtnd.lus.base.ItemViewHolder
import com.vtnd.lus.data.TokenRepository
import com.vtnd.lus.data.UserRepository
import com.vtnd.lus.data.repository.source.remote.api.request.IdolResponse
import com.vtnd.lus.shared.TYPE_HEADER
import com.vtnd.lus.shared.liveData.SingleLiveData
import com.vtnd.lus.shared.type.CategoryIdolType

class HomeViewModel(
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository
) : BaseViewModel() {
    val hotIdols = SingleLiveData<List<IdolResponse>>()
    val storyIdols = SingleLiveData<List<ItemViewHolder<Any>>>()

    init {
        initialStoryIdols()
        initialHotIdols()
    }

    private fun initialStoryIdols() {
        mutableListOf<ItemViewHolder<Any>>().apply {
            add(ItemViewHolder(type = TYPE_HEADER, itemData = Any()))
            for (i in 2..10) add(ItemViewHolder(Any()))
        }.let(storyIdols::postValue)
    }

    private fun initialHotIdols() {
        viewModelScope(hotIdols,
            onRequest = {
                userRepository.getIdols(!tokenRepository.getToken().isNullOrEmpty(),
                    CategoryIdolType.RATING)
            },
            onSuccess = { hotIdols.postValue(it) },
            onError = { exception.postValue(it) }
        )
    }
}
