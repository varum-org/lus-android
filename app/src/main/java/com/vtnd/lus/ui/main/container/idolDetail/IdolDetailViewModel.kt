package com.vtnd.lus.ui.main.container.idolDetail

import androidx.lifecycle.viewModelScope
import com.vtnd.lus.base.BaseViewModel
import com.vtnd.lus.data.model.Service
import com.vtnd.lus.shared.liveData.SingleLiveData
import com.vtnd.lus.shared.type.CardActionType
import com.vtnd.lus.ui.main.container.idolDetail.adapter.ItemCard
import com.vtnd.lus.ui.main.container.idolDetail.adapter.ItemService
import kotlinx.coroutines.launch

class IdolDetailViewModel : BaseViewModel() {
    val cardServicesLiveData = SingleLiveData<List<ItemCard>>()
    val idolServicesLiveData = SingleLiveData<List<ItemService>>()
    private var cardServices = mutableListOf<ItemCard>()
    private var idolServices = mutableListOf<ItemService>()

    fun addServiceToCard(itemService: ItemService, pos: Int) {
        viewModelScope.launch {
            val oldService =
                cardServices.singleOrNull { it.service.serviceCode == itemService.service.serviceCode }
            cardServices =
                if (oldService == null) ArrayList(cardServices).apply { add(ItemCard(itemService.service)) }
                else ArrayList(cardServices.filter { it.service.serviceCode !== itemService.service.serviceCode })
            cardServicesLiveData.postValue(cardServices)
            updateIdolService(itemService.service)
        }
    }

    fun handleHoursService(actionType: CardActionType, itemCard: ItemCard) {
        viewModelScope.launch {
            when (actionType) {
                CardActionType.MINUS -> {
                    val oldService =
                        cardServices.singleOrNull { it.service.serviceCode == itemCard.service.serviceCode }
                    if (oldService != null)
                        if (oldService.hours - 1 == 0) {
                            cardServices =
                                ArrayList(cardServices.filter { it.service.serviceCode !== itemCard.service.serviceCode })
                            updateIdolService(itemCard.service)
                        } else {
                            cardServices = ArrayList(cardServices.map {
                                if (it.service.serviceCode == itemCard.service.serviceCode)
                                    it.copy(hours = it.hours - 1)
                                else it
                            })
                        }
                }
                CardActionType.PLUS -> {
                    cardServices = ArrayList(cardServices.map {
                        if (it.service.serviceCode == itemCard.service.serviceCode)
                            it.copy(hours = it.hours + 1)
                        else it
                    })
                }
            }
        }
        cardServicesLiveData.postValue(cardServices)
    }

    fun addServicesIdol(services: List<Service>) {
        viewModelScope.launch {
            idolServices.addAll(services.map { ItemService(it) })
            idolServicesLiveData.postValue(idolServices)
        }
    }

    fun removeAllServiceFromCard() {
        cardServices.clear()
        cardServicesLiveData.postValue(cardServices)
    }

    private fun updateIdolService(service: Service) {
        idolServices = ArrayList(idolServices.map {
            if (it.service.serviceCode == service.serviceCode)
                it.copy(selected = !it.selected)
            else it
        })
        idolServicesLiveData.postValue(idolServices)
    }
}
