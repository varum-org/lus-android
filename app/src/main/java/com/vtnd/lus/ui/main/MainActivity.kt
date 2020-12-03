package com.vtnd.lus.ui.main

import android.view.LayoutInflater
import com.vtnd.lus.R
import com.vtnd.lus.base.BaseActivity
import com.vtnd.lus.databinding.ActivityMainBinding
import com.vtnd.lus.di.MainApplication
import com.vtnd.lus.shared.extensions.addFragmentToActivity
import com.vtnd.lus.shared.liveData.observeLiveData
import com.vtnd.lus.ui.main.container.ContainerFragment
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {
    lateinit var socket: Socket
    private var userId :String? = null

    override val viewModel: MainViewModel by viewModel()

    override fun inflateViewBinding(inflater: LayoutInflater) =
        ActivityMainBinding.inflate(inflater)

    override fun initialize() {
        socket=(application as MainApplication).socket
        addFragmentToActivity(R.id.container, ContainerFragment.newInstance(), false)
    }

    @ExperimentalCoroutinesApi
    override fun registerLiveData() = with(viewModel) {
        super.registerLiveData()
        userObservable.observeLiveData(this@MainActivity) {
            it?.let { user ->
                userId = user.id
                socket.connect()
                    .on(Socket.EVENT_CONNECT) {
                        socket.emit(SOCKET_JOIN_ROOM, user.id)
                    }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        socket.apply {
            userId?.let {
                emit(SOCKET_OFFLINE,userId)
            }
            disconnect()
        }
    }
    companion object{
        private const val SOCKET_JOIN_ROOM = "join"
        private const val SOCKET_OFFLINE = "offline"
    }
}
