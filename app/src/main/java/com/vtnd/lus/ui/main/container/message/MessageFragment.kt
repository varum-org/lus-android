package com.vtnd.lus.ui.main.container.message

import android.system.Os.socket
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
import com.vtnd.lus.shared.extensions.setupDismissKeyBoard
import com.vtnd.lus.shared.extensions.showError
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
    lateinit var roomId: String
    var onUpdateChat = Emitter.Listener {
        viewModel.stringToMessage(userId, it[0].toString())
    }
    private val messageAdapter by lazy {
        MessageAdapter {}
    }


    override val viewModel: MessageViewModel by viewModel()

    override fun inflateViewBinding(inflater: LayoutInflater) =
        FragmentMessageBinding.inflate(inflater)

    override fun initialize() {
        setupDismissKeyBoard(requireActivity(), messageLayout)
        socket = (requireActivity().application as MainApplication).socket
        arguments?.apply {
            getParcelable<Room>(ARGUMENT_ROOM)?.let(viewModel::postRoom)
        }
        initToolbarBase(
            getString(R.string.message),
            iconRight = R.drawable.ic_baseline_settings_24,
            isShowIconLeft = true
        )
        sendImage.safeClick {
            handleSendImage()
        }
        onSocketMessage()
        messageRecycleView.apply {
            setHasFixedSize(true)
            adapter = messageAdapter
        }
    }

    override fun registerLiveData() = with(viewModel) {
        super.registerLiveData()
        roomLiveData.observeLiveData(viewLifecycleOwner) {
            roomId = it.id.toString()
        }
        userLiveData.observeLiveData(viewLifecycleOwner) {
            userId = it.id.toString()
        }
        messageJson.observeLiveData(viewLifecycleOwner) {
            if (!it.isNullOrEmpty())
                socket.emit(SOCKET_CLIENT_SEND, it)
        }
        messagesLiveData.observeLiveData(viewLifecycleOwner) {
            if (it.isEmpty()) {
                showChildNoDataFragment(R.id.messageLayout)
            } else {
                messageAdapter.submitList(it)
            }
        }
    }

    override fun onStop() {
        onHideSoftKeyBoard()
        super.onStop()
    }

    private fun handleSendImage() {
        if (messageEditText.text.isNullOrEmpty()) {
            showError(getString(R.string.please_enter_your_message))
        } else {
            val message = Message("", userId, messageEditText.text.toString(), roomId)
            viewModel.messageToString(message)
        }
    }

    private fun onSocketMessage() {
        socket.on(SOCKET_SERVER_SEND, onUpdateChat)
    }

    companion object {
        const val ARGUMENT_ROOM = "ARGUMENT_ROOM"
        const val SOCKET_SERVER_SEND = "serverSendMessage"
        const val SOCKET_CLIENT_SEND = "clientSendMessage"

        fun newInstance(room: Room) = MessageFragment().apply {
            arguments = bundleOf(ARGUMENT_ROOM to room)
        }
    }
}
