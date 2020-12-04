package com.vtnd.lus.ui.main.container.room

import com.vtnd.lus.base.BaseViewModel
import com.vtnd.lus.data.UserRepository
import com.vtnd.lus.data.model.Room
import com.vtnd.lus.data.repository.source.remote.api.response.RoomResponse
import com.vtnd.lus.shared.liveData.SingleLiveData

class RoomViewModel(private val userRepository: UserRepository) : BaseViewModel(){

    val rooms = SingleLiveData<List<RoomResponse>>()

    init {
        viewModelScope(rooms,
        onRequest = {userRepository.getRooms()},
        onSuccess = {rooms.postValue(it)},
        onError = {exception.postValue(it)})
    }
}
