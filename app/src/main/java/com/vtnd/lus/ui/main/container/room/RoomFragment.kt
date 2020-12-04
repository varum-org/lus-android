package com.vtnd.lus.ui.main.container.room

import android.view.LayoutInflater
import com.vtnd.lus.R
import com.vtnd.lus.base.BaseFragment
import com.vtnd.lus.base.ItemViewHolder
import com.vtnd.lus.databinding.FragmentRoomBinding
import com.vtnd.lus.shared.AnimateType
import com.vtnd.lus.shared.decoration.VerticalSpaceItemDecoration
import com.vtnd.lus.shared.extensions.initToolbarBase
import com.vtnd.lus.shared.extensions.replaceFragment
import com.vtnd.lus.shared.liveData.observeLiveData
import com.vtnd.lus.ui.main.container.message.MessageFragment
import com.vtnd.lus.ui.main.container.room.adapter.RoomAdapter
import kotlinx.android.synthetic.main.fragment_room.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class RoomFragment : BaseFragment<FragmentRoomBinding,
        RoomViewModel>() {
    private val roomAdapter by lazy {
        RoomAdapter() {
            replaceFragment(
                R.id.container,
                MessageFragment.newInstance(it.room!!),
                addToBackStack = true,
                animateType = AnimateType.SLIDE_TO_RIGHT
            )
        }
    }
    override val viewModel: RoomViewModel by viewModel()

    override fun inflateViewBinding(inflater: LayoutInflater) =
        FragmentRoomBinding.inflate(inflater)

    override fun initialize() {
        viewModel.getUser {
            it?.let { user ->
                roomAdapter.user = user
            }
        }
        initToolbarBase(
            getString(R.string.message),
            iconRight = R.drawable.ic_baseline_settings_24,
            isShowIconLeft = true
        )
        roomRecyclerView.apply {
            setHasFixedSize(true)
            addItemDecoration(VerticalSpaceItemDecoration(resources.getDimensionPixelOffset(R.dimen.dp_1)))
            adapter = roomAdapter
        }
    }

    override fun registerLiveData() = with(viewModel) {
        super.registerLiveData()
        rooms.observeLiveData(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) showChildNoDataFragment(R.id.roomLayout)
            else {
                hideChildNoDataFragment()
                roomAdapter.submitList(it.map { item -> ItemViewHolder(item) })
            }
        }
    }

    companion object {
        fun newInstance() = RoomFragment()
    }
}
