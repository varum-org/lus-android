package com.vtnd.lus.ui.main.container.history.tab

import com.vtnd.lus.base.BaseViewModel
import com.vtnd.lus.data.UserRepository
import com.vtnd.lus.data.model.Order
import com.vtnd.lus.shared.liveData.SingleLiveData

class TabHistoryViewModel(private val userRepository: UserRepository) : BaseViewModel() {
    val historys = SingleLiveData<List<Order>>()
    val newOrder = SingleLiveData<Any>()

    fun getOder(status: Int) {
        viewModelScope(historys,
            onRequest = {
                userRepository.getOrders(status)
            }, onSuccess = {
                historys.postValue(it)
            }, onError = {
                exception.postValue(it)
            })
    }

    fun deleteOrder(orderId: String) {
        viewModelScope(newOrder,
            onRequest = {
                userRepository.deleteOrder(orderId)
            }, onSuccess = {
                newOrder.postValue(it)
            }, onError = {
                exception.postValue(it)
            })
    }
}
