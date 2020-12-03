package com.vtnd.lus.ui.main.container.message

import com.vtnd.lus.base.BaseViewModel
import com.vtnd.lus.data.model.Room
import com.vtnd.lus.shared.liveData.SingleLiveData

class MessageViewModel : BaseViewModel() {
    val roomLiveData = SingleLiveData<Room>()

    fun postRoom(room: Room) = roomLiveData.postValue(room)
}
