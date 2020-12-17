package com.vtnd.lus.ui.main.container.favorite

import com.vtnd.lus.base.BaseViewModel
import com.vtnd.lus.data.TokenRepository
import com.vtnd.lus.data.UserRepository
import com.vtnd.lus.data.repository.source.remote.api.response.IdolResponse
import com.vtnd.lus.shared.liveData.SingleLiveData
import com.vtnd.lus.shared.type.CategoryIdolType

class FavoriteViewModel(
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository
) : BaseViewModel() {
    val favorites = SingleLiveData<List<IdolResponse>>()

    init {
        initialHotIdols()
    }

    private fun initialHotIdols() {
        viewModelScope(
            favorites,
            onRequest = {
                userRepository.getIdols(
                    !tokenRepository.getToken().isNullOrEmpty(),
                    CategoryIdolType.RANDOM
                )
            },
            onSuccess = {
                favorites.postValue(it)
            },
            onError = { exception.postValue(it) }
        )
    }
}
