package com.vtnd.lus.ui.main.container.favorite

import com.vtnd.lus.base.BaseViewModel
import com.vtnd.lus.shared.liveData.SingleLiveData

class FavoriteViewModel : BaseViewModel() {
    val favorites = SingleLiveData<List<Any>>()

    init {
        favorites.postValue(listOf(
                Any(),
                Any(),
                Any(),
                Any()
        ))
    }
}
