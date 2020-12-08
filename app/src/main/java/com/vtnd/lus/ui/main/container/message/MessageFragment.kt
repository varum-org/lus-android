package com.vtnd.lus.ui.main.container.message

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import androidx.core.os.bundleOf
import com.vtnd.lus.R
import com.vtnd.lus.base.BaseFragment
import com.vtnd.lus.data.model.Message
import com.vtnd.lus.data.model.Room
import com.vtnd.lus.databinding.FragmentMessageBinding
import com.vtnd.lus.di.MainApplication
import com.vtnd.lus.shared.extensions.initToolbarBase
import com.vtnd.lus.shared.extensions.safeClick
import com.vtnd.lus.shared.liveData.observeLiveData
import com.vtnd.lus.ui.main.container.message.adapter.MessageAdapter
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.fragment_message.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MessageFragment : BaseFragment<FragmentMessageBinding, MessageViewModel>() {
    lateinit var socket: Socket
    lateinit var room: Room
    lateinit var userId: String

    var onUpdateChat = Emitter.Listener {
        viewModel.stringToMessage(it[0].toString())
    }
    private val messageAdapter by lazy {
        MessageAdapter {}
    }

    override val viewModel: MessageViewModel by viewModel()

    override fun inflateViewBinding(inflater: LayoutInflater) =
        FragmentMessageBinding.inflate(inflater)

    @SuppressLint("ClickableViewAccessibility")
    override fun initialize() {
        socket = (requireActivity().application as MainApplication).socket
        arguments?.apply {
            getParcelable<Room>(ARGUMENT_ROOM)?.let {
                viewModel.postRoom(it)
            }
        }
        initToolbarBase(
            getString(R.string.message),
            iconRight = R.drawable.ic_baseline_settings_24,
            isShowIconLeft = true
        )
        messageRecycleView.apply {
            setHasFixedSize(true)
            adapter = messageAdapter
            setOnTouchListener { _, _ ->
                onHideSoftKeyBoard()
                false
            }
        }
        onSocketMessage()
        sendImage.safeClick {
            handleSendImage()
        }
        messageEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isNotEmpty()) {
                    socket.emit(SOCKET_TYPING, room.id)
                } else {
                    socket.emit(SOCKET_STOP_TYPING, room.id)
                }
            }
        })
    }

    override fun registerLiveData() = with(viewModel) {
        super.registerLiveData()
        roomLiveData.observeLiveData(viewLifecycleOwner) {
            room = it
            socket.emit(SOCKET_JOIN_CHAT, room.id)
        }
        userLiveData.observeLiveData(viewLifecycleOwner) {
            userId = it!!.user?.id.toString()
        }
        messageJson.observeLiveData(viewLifecycleOwner) {
            if (!it.isNullOrEmpty())
                socket.emit(SOCKET_CLIENT_SEND, it)
        }
        messagesLiveData.observeLiveData(viewLifecycleOwner) {
            if (it.isEmpty()) {
                showChildNoDataFragment(R.id.messageLayout)
            } else {
                messageAdapter.submitList(it){
                    messageRecycleView.scrollToPosition(it.size - 1)
                }
                hideChildNoDataFragment()
            }
        }
    }

    override fun onStop() {
        onHideSoftKeyBoard()
        super.onStop()
    }

    private fun handleSendImage() {
        if (!messageEditText.text.isNullOrEmpty() && messageEditText.text.isNotBlank()) {
            val message = Message("", userId, messageEditText.text.toString(), room.id)
            viewModel.messageToString(message)
            messageEditText.text.clear()
        }
    }

    private fun onSocketMessage() {
        socket.on(SOCKET_SERVER_SEND, onUpdateChat)
        socket.on(SOCKET_TYPING) {
            viewModel.typing()
        }
        socket.on(SOCKET_STOP_TYPING) {
            viewModel.stopTyping()
        }
        socket.on(SOCKET_JOIN_CHAT) {
            viewModel.joinLeaveChat(it[0].toString())
        }
        socket.on(SOCKET_LEAVE_CHAT) {
            viewModel.joinLeaveChat(it[0].toString())
        }
    }

    override fun onDestroyView() {
        socket.emit(SOCKET_LEAVE_CHAT, room.id)
        super.onDestroyView()
    }

    companion object {
        const val ARGUMENT_ROOM = "ARGUMENT_ROOM"
        const val SOCKET_SERVER_SEND = "serverSendMessage"
        const val SOCKET_CLIENT_SEND = "clientSendMessage"
        const val SOCKET_TYPING = "typing"
        const val SOCKET_STOP_TYPING = "stopTyping"
        const val SOCKET_JOIN_CHAT = "joinChat"
        const val SOCKET_LEAVE_CHAT = "leaveChat"

        fun newInstance(room: Room) = MessageFragment().apply {
            arguments = bundleOf(ARGUMENT_ROOM to room)
        }
    }
}
