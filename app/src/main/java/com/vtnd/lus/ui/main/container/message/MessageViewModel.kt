package com.vtnd.lus.ui.main.container.message

import androidx.lifecycle.viewModelScope
import com.vtnd.lus.base.BaseViewModel
import com.vtnd.lus.base.ItemViewHolder
import com.vtnd.lus.data.UserRepository
import com.vtnd.lus.data.model.Message
import com.vtnd.lus.data.model.MessageJsonAdapter
import com.vtnd.lus.data.model.Room
import com.vtnd.lus.data.model.User
import com.vtnd.lus.shared.liveData.SingleLiveData
import com.vtnd.lus.shared.scheduler.dispatcher.AppDispatchers
import com.vtnd.lus.shared.scheduler.dispatcher.DispatchersProvider
import com.vtnd.lus.shared.type.MessageType
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.get
import org.koin.core.qualifier.named

class MessageViewModel(
    private val userRepository: UserRepository,
    private val messageJsonAdapter: MessageJsonAdapter
) : BaseViewModel(), KoinComponent {
    private val dispatchersProvider =
        get<DispatchersProvider>(named(AppDispatchers.MAIN)).dispatcher()
    val roomLiveData = SingleLiveData<Room>()
    val userLiveData = SingleLiveData<User>()
    val messageJson = SingleLiveData<String>()
    val messagesLiveData = SingleLiveData<List<ItemViewHolder<Message>>>()
    private val messages = mutableListOf<Message>()

    init {
        viewModelScope.launch(dispatchersProvider) {
            userLiveData.postValue(userRepository.user())
        }
    }

    fun postRoom(room: Room) {
        roomLiveData.postValue(room)
        viewModelScope(
            null,
            onRequest = { userRepository.getMessageFromRoom(room.id!!) },
            onSuccess = {
                messagesLiveData.postValue(
                    ArrayList(messages.apply { addAll(it) }).map {
                        if (userLiveData.value?.id == it.userId)
                            ItemViewHolder(it).copy(type = MessageType.CHAT_MINE.type)
                        else ItemViewHolder(it).copy(type = MessageType.CHAT_PARTNER.type)
                    }
                )
            },
            onError = { exception.postValue(it) })
    }

    fun messageToString(message: Message) {
        messageJson.postValue(messageJsonAdapter.toJson(message))
    }

    fun stringToMessage(userId: String, message: String) {
        messageJsonAdapter.fromJson(message)?.let { newMessage ->
            if (roomLiveData.value?.id == newMessage.roomId) {
                messages.add(newMessage)
                val newMessages = ArrayList(messages).map {
                    if (userId == it.userId)
                        ItemViewHolder(it).copy(type = MessageType.CHAT_MINE.type)
                    else ItemViewHolder(it).copy(type = MessageType.CHAT_PARTNER.type)
                }
                messagesLiveData.postValue(newMessages)
            }
        }
    }
}
