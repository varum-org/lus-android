package com.vtnd.lus.ui.main.container.message

import android.view.LayoutInflater
import androidx.core.os.bundleOf
import com.vtnd.lus.R
import com.vtnd.lus.base.BaseFragment
import com.vtnd.lus.data.model.Room
import com.vtnd.lus.databinding.FragmentMessageBinding
import com.vtnd.lus.shared.extensions.initToolbarBase
import com.vtnd.lus.shared.liveData.observeLiveData
import org.koin.androidx.viewmodel.ext.android.viewModel

class MessageFragment : BaseFragment<FragmentMessageBinding, MessageViewModel>() {

    override val viewModel: MessageViewModel by viewModel()

    override fun inflateViewBinding(inflater: LayoutInflater) =
        FragmentMessageBinding.inflate(inflater)

    override fun initialize() {
        arguments?.apply {
            getParcelable<Room>(ARGUMENT_ROOM)?.let(viewModel::postRoom)
        }
//        showChildNoDataFragment(R.id.messageLayout)
        initToolbarBase(
            getString(R.string.message),
            iconRight = R.drawable.ic_baseline_settings_24,
            isShowIconLeft = true
        ) {

        }
    }

    override fun registerLiveData() = with(viewModel) {
        super.registerLiveData()
        roomLiveData.observeLiveData(viewLifecycleOwner) {

        }
    }

    companion object {
        const val ARGUMENT_ROOM = "ARGUMENT_ROOM"

        fun newInstance(room: Room) = MessageFragment().apply {
            arguments = bundleOf(ARGUMENT_ROOM to room)
        }
    }
}
