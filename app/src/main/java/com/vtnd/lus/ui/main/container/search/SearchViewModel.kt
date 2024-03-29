package com.vtnd.lus.ui.main.container.search

import com.vtnd.lus.base.BaseViewModel
import com.vtnd.lus.data.UserRepository
import com.vtnd.lus.data.model.Idol
import com.vtnd.lus.shared.liveData.SingleLiveData

class SearchViewModel(private val userRepository: UserRepository) : BaseViewModel() {
    val searchIdols = SingleLiveData<List<Idol>>()
    var query: String

    init {
        query = ""
        searchIdols.postValue(emptyList())
    }

    fun search(name: String, rating: Int? = null) {
        if (name.trim() != query.trim()) {
            if (name.isNotBlank())
                viewModelScope(searchIdols,
                    isShowLoading = false,
                    onRequest = {
                        userRepository.search(name.trim(), rating)
                    },
                    onSuccess = {
                        query = name.trim()
                        searchIdols.postValue(it)
                    },
                    onError = { exception.postValue(it) })
            else {
                query = ""
                searchIdols.postValue(emptyList())
            }
        }
    }
}
