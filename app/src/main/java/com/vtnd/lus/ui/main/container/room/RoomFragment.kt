package com.vtnd.lus.ui.main.container.room

import android.view.LayoutInflater
import com.vtnd.lus.R
import com.vtnd.lus.base.BaseFragment
import com.vtnd.lus.databinding.FragmentNotificationBinding
import com.vtnd.lus.databinding.FragmentRoomBinding
import com.vtnd.lus.shared.extensions.initToolbarBase
import org.koin.androidx.viewmodel.ext.android.viewModel

class RoomFragment : BaseFragment<FragmentRoomBinding,
        RoomViewModel>() {

    override val viewModel: RoomViewModel by viewModel()

    override fun inflateViewBinding(inflater: LayoutInflater) =
        FragmentRoomBinding.inflate(inflater)

    override fun initialize() {
        showChildNoDataFragment(R.id.roomLayout)
        initToolbarBase(
            getString(R.string.message),
            iconRight = R.drawable.ic_baseline_settings_24,
            isShowIconLeft = true
        )
    }

    override fun registerLiveData() {
        super.registerLiveData()
    }

    companion object {
        fun newInstance() = RoomFragment()
    }
}
