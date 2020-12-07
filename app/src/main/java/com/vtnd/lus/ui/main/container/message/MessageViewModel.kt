package com.vtnd.lus.ui.main.container.message

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.vtnd.lus.base.BaseViewModel
import com.vtnd.lus.base.ItemViewHolder
import com.vtnd.lus.data.UserRepository
import com.vtnd.lus.data.model.*
import com.vtnd.lus.shared.extensions.isNull
import com.vtnd.lus.shared.liveData.SingleLiveData
import com.vtnd.lus.shared.scheduler.dispatcher.AppDispatchers
import com.vtnd.lus.shared.scheduler.dispatcher.DispatchersProvider
import com.vtnd.lus.shared.type.MessageType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.get
import org.koin.core.qualifier.named

class MessageViewModel(
    private val userRepository: UserRepository,
    private val messageJsonAdapter: MessageJsonAdapter,
    private val roomJsonAdapter: RoomJsonAdapter
) : BaseViewModel(), KoinComponent {
    private val dispatchersProvider =
        get<DispatchersProvider>(named(AppDispatchers.MAIN)).dispatcher()
    val roomLiveData = SingleLiveData<Room>()
    val messageJson = SingleLiveData<String>()
    val messagesLiveData = SingleLiveData<List<ItemViewHolder<Message>>>()
    private var messages = mutableListOf<ItemViewHolder<Message>>()
    var roomSize = 0

    @ExperimentalCoroutinesApi
    val userLiveData: LiveData<User?> = userRepository.userObservable()
        .map { it }
        .distinctUntilChanged()
        .flowOn(dispatchersProvider)
        .buffer(1)
        .asLiveData()

    fun postRoom(room: Room) {
        roomLiveData.postValue(room)
        viewModelScope(
            null,
            onRequest = { userRepository.getMessageFromRoom(room.id!!) },
            onSuccess = {
                viewModelScope.launch {
                    messages.addAll(it.map { message ->
                        if (userRepository.user()?.id == message.userId)
                            ItemViewHolder(message).copy(type = MessageType.CHAT_MINE.type)
                        else ItemViewHolder(message).copy(type = MessageType.CHAT_PARTNER.type)
                    }).apply {
                        messagesLiveData.postValue(ArrayList(messages))
                    }
                }
            },
            onError = { exception.postValue(it) })
    }

    @ExperimentalCoroutinesApi
    fun joinLeaveChat(roomString: String) {
        roomJsonAdapter.fromJson(roomString)?.let { room ->
            if (roomLiveData.value?.id == room.id) {
                    roomSize = room.size
                    if (roomSize > 1) {
                        updateRoom()
                    }
            }
        }
    }

    private fun updateRoom() {
        messages = ArrayList(messages.map {
            it.copy(itemData = it.itemData.copy(isRead = 1))
        })
        messagesLiveData.postValue(messages)
    }

    fun messageToString(message: Message) {
        messages.add(
            ItemViewHolder(
                message.copy(isRead = if (roomSize > 1) 1 else 0)
            ).copy(type = MessageType.CHAT_MINE.type)
        )
        messagesLiveData.postValue(ArrayList(messages))
        messageJson.postValue(messageJsonAdapter.toJson(message))
    }

    fun stringToMessage(message: String) {
        messageJsonAdapter.fromJson(message)?.let { newMessage ->
            if (roomLiveData.value?.id == newMessage.roomId) {
                messages.add(ItemViewHolder(newMessage).copy(type = MessageType.CHAT_PARTNER.type))
                val newMessages = ArrayList(messages)
                messagesLiveData.postValue(newMessages)
            }
        }
    }

    fun typing() {
        val typing = messages.singleOrNull { it.type == MessageType.TYPING.type }
        typing.isNull {
            messages.add(ItemViewHolder(Message()).copy(type = MessageType.TYPING.type))
            val newMessages = ArrayList(messages)
            messagesLiveData.postValue(newMessages)
        }
    }

    fun stopTyping() {
        messages.remove(ItemViewHolder(Message()).copy(type = MessageType.TYPING.type))
        messagesLiveData.postValue(ArrayList(messages))
    }
}
