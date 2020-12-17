package com.vtnd.lus.ui.main.container.historyIdol.tab

import com.vtnd.lus.base.BaseViewModel
import com.vtnd.lus.data.UserRepository
import com.vtnd.lus.data.model.Order
import com.vtnd.lus.data.repository.source.remote.api.request.HistoryRequest
import com.vtnd.lus.shared.liveData.SingleLiveData

class TabHistoryIdolViewModel(private val userRepository: UserRepository) : BaseViewModel() {
    val historyIdols = SingleLiveData<List<Order>>()
    val newOrder = SingleLiveData<Order>()

    fun getOder(status: Int) {
        viewModelScope(historyIdols,
            onRequest = {
                userRepository.getOrdersUser(status)
            }, onSuccess = {
                historyIdols.postValue(it)
            }, onError = {
                exception.postValue(it)
            })
    }

    fun updateStatus(status: Int, orderId: String) {
        viewModelScope(newOrder,
            onRequest = {
                userRepository.updateOder(HistoryRequest(orderId, status))
            }, onSuccess = {
                newOrder.postValue(it)
            }, onError = {
                exception.postValue(it)
            })
    }
}
