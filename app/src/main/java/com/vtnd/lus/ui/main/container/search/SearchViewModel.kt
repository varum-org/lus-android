package com.vtnd.lus.ui.main.container.search

import com.vtnd.lus.base.BaseViewModel
import com.vtnd.lus.shared.liveData.SingleLiveData

class SearchViewModel : BaseViewModel() {
    val searchidols = SingleLiveData<List<Any>>()

    init {
        searchidols.postValue(listOf(
                Any(),
                Any(),
                Any(),
                Any(),
                Any(),
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
        ))
    }
}
