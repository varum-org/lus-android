package com.vtnd.lus.ui.main.container.room

import androidx.lifecycle.viewModelScope
import com.vtnd.lus.base.BaseViewModel
import com.vtnd.lus.data.UserRepository
import com.vtnd.lus.data.model.Room
import com.vtnd.lus.data.model.RoomJsonAdapter
import com.vtnd.lus.data.model.User
import com.vtnd.lus.data.repository.source.remote.api.response.RoomResponse
import com.vtnd.lus.data.repository.source.remote.api.response.RoomResponseJsonAdapter
import com.vtnd.lus.shared.liveData.SingleLiveData
import kotlinx.coroutines.launch
import timber.log.Timber

class RoomViewModel(
    private val userRepository: UserRepository,
    private val roomJsonAdapter: RoomJsonAdapter,
    private val roomResponseJsonAdapter: RoomResponseJsonAdapter
) : BaseViewModel() {

    val roomsLiveData = SingleLiveData<List<RoomResponse>>()
    val roomJson = SingleLiveData<String>()
    var rooms = mutableListOf<RoomResponse>()

    init {
        viewModelScope(roomsLiveData,
            onRequest = { userRepository.getRooms() },
            onSuccess = {
                rooms.addAll(it)
                roomsLiveData.postValue(ArrayList(rooms))
            },
            onError = { exception.postValue(it) })
    }

    fun postRoom(room: Room) = roomJson.postValue(roomJsonAdapter.toJson(room))

    fun postRoomResponse(room: String) {
        roomResponseJsonAdapter.fromJson(room)?.let {
            rooms.add(it)
            roomsLiveData.postValue(ArrayList(rooms))
        }
    }

    fun getUser(user: (User?) -> Unit) {
        viewModelScope.launch {
            user.invoke(userRepository.user())
        }
    }
}
