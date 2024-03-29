package com.vtnd.lus.ui.main.container.idolDetail

import androidx.lifecycle.viewModelScope
import com.vtnd.lus.base.BaseViewModel
import com.vtnd.lus.data.TokenRepository
import com.vtnd.lus.data.UserRepository
import com.vtnd.lus.data.model.Room
import com.vtnd.lus.data.model.RoomJsonAdapter
import com.vtnd.lus.data.model.Service
import com.vtnd.lus.data.repository.source.remote.api.request.OrderRequest
import com.vtnd.lus.data.repository.source.remote.api.request.RoomRequest
import com.vtnd.lus.data.repository.source.remote.api.response.IdolResponse
import com.vtnd.lus.shared.extensions.sumPrice
import com.vtnd.lus.shared.liveData.SingleLiveData
import com.vtnd.lus.shared.scheduler.dispatcher.AppDispatchers
import com.vtnd.lus.shared.scheduler.dispatcher.DispatchersProvider
import com.vtnd.lus.shared.type.CardActionType
import com.vtnd.lus.ui.main.container.idolDetail.adapter.ItemCard
import com.vtnd.lus.ui.main.container.idolDetail.adapter.ItemService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.get
import org.koin.core.qualifier.named
import java.util.*
import kotlin.collections.ArrayList

class IdolDetailViewModel(
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository,
    private val roomJsonAdapter: RoomJsonAdapter
) : BaseViewModel(), KoinComponent {
    private val dispatchersProvider =
        get<DispatchersProvider>(named(AppDispatchers.MAIN)).dispatcher()
    val cardServicesLiveData = SingleLiveData<List<ItemCard>>()
    val idolServicesLiveData = SingleLiveData<List<ItemService>>()
    val idolResponseLiveData = SingleLiveData<IdolResponse>()
    val startDate = SingleLiveData<Date>()
    val note = SingleLiveData<String>()
    private var cardServices = mutableListOf<ItemCard>()
    private var idolServices = mutableListOf<ItemService>()
    val room = SingleLiveData<Room>()
    val roomJson = SingleLiveData<String>()
    val responseOrder = SingleLiveData<Any>()

    init {
        startDate.postValue(Date())
    }

    @ExperimentalCoroutinesApi
    fun checkLogin(check: (Boolean) -> Unit) {
        viewModelScope.launch {
            check.invoke(!tokenRepository.getToken().isNullOrEmpty())
        }
    }

    fun getRoom(user_id: String) {
        viewModelScope(room,
            onRequest = { userRepository.getRoom(RoomRequest(user_id)) },
            onSuccess = {
                roomJson.postValue(roomJsonAdapter.toJson(it))
                room.postValue(it)
                        },
            onError = { exception.postValue(it) })
    }

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
            if (idolServices.isNullOrEmpty()) {
                idolServices.addAll(services.map { ItemService(it) })
                idolServicesLiveData.postValue(idolServices)
            }
        }
    }

    fun getIdolDetail(id: String) {
        viewModelScope(idolResponseLiveData,
            onRequest = { userRepository.getIdol(id) },
            onSuccess = { idolResponseLiveData.postValue(it) },
            onError = { exception.postValue(it) })
    }

    fun oderServices() {
        viewModelScope(responseOrder,
            onRequest = {
                userRepository.order(OrderRequest(
                    user_id = idolResponseLiveData.value?.idol?.userId!!,
                    startDate = startDate.value!!,
                    note = note.value.toString(),
                    services = cardServices.map {
                        Service(
                            serviceCode = it.service.serviceCode,
                            serviceName = it.service.serviceName,
                            servicePrice = it.service.servicePrice,
                            hour = it.hours
                        )
                    }
                ))
            },
            onSuccess = { responseOrder.postValue(it) },
            onError = { exception.postValue(it) })
    }

    fun removeAllServiceFromCard() {
        cardServices = mutableListOf()
        cardServicesLiveData.postValue(ArrayList(cardServices))
        idolServices = ArrayList(idolServices.map {
            if (it.selected) it.copy(selected = !it.selected)
            else it
        })
        idolServicesLiveData.postValue(idolServices)
    }

    private fun updateIdolService(service: Service) {
        idolServices = ArrayList(idolServices.map {
            if (it.service.serviceCode == service.serviceCode)
                it.copy(selected = !it.selected)
            else it
        })
        idolServicesLiveData.postValue(idolServices)
    }

    fun checkWallet(check: (Boolean) -> Unit) {
        viewModelScope.launch(dispatchersProvider) {
            userRepository.user()?.user?.wallet?.let {
                check.invoke(cardServices.sumPrice() <= it)
            }
        }
    }
}
