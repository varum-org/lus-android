package com.vtnd.lus.ui.main.container.historyIdol.tab

import android.view.LayoutInflater
import com.vtnd.lus.R
import com.vtnd.lus.base.BaseFragment
import com.vtnd.lus.base.ItemViewHolder
import com.vtnd.lus.databinding.FragmentTabHistoryIdolBinding
import com.vtnd.lus.shared.extensions.showNotificationAlertDialog
import com.vtnd.lus.shared.liveData.observeLiveData
import com.vtnd.lus.shared.type.HistoryActionType
import com.vtnd.lus.ui.main.container.historyIdol.adapter.HistoryIdolAdapter
import kotlinx.android.synthetic.main.fragment_tab_history_idol.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class TabHistoryIdolFragment : BaseFragment<FragmentTabHistoryIdolBinding, TabHistoryIdolViewModel>() {
    private lateinit var historyIdolAdapter: HistoryIdolAdapter
    override val viewModel : TabHistoryIdolViewModel by viewModel()

    override fun inflateViewBinding(inflater: LayoutInflater) =
        FragmentTabHistoryIdolBinding.inflate(inflater)

    override fun initialize() {
        arguments?.takeIf { it.containsKey(ARG_STATUS_CODE_IDOL) }?.apply {
            viewModel.getOder(getInt(ARG_STATUS_CODE_IDOL))
            historyIdolAdapter = HistoryIdolAdapter(getInt(ARG_STATUS_CODE_IDOL)) { type, order ->
                when (type) {
                    HistoryActionType.REJECT -> {
                        viewModel.updateStatus(3, order.id!!)
                    }
                    HistoryActionType.ACCEPT -> {
                        viewModel.updateStatus(2, order.id!!)
                    }
                    HistoryActionType.DONE -> {
                        viewModel.updateStatus(4, order.id!!)
                    }
                }
            }
        }
        tabHistoryIdolRecyclerView.apply {
            adapter = historyIdolAdapter
        }
    }

    override fun registerLiveData() = with(viewModel) {
        super.registerLiveData()
        historyIdols.observeLiveData(viewLifecycleOwner) {
            historyIdolAdapter.submitList(it.map { oder ->
                ItemViewHolder(oder)
            })
        }
        newOrder.observeLiveData(viewLifecycleOwner) {
            showNotificationAlertDialog {
                icon(R.drawable.ic_check)
                statusMessage(getString(R.string.success))
                button(getString(R.string.ok2)) {
                    arguments?.takeIf { it.containsKey(ARG_STATUS_CODE_IDOL) }?.apply {
                        viewModel.getOder(getInt(ARG_STATUS_CODE_IDOL))
                    }
                }
            }
        }
    }

    companion object {
        const val ARG_STATUS_CODE_IDOL = "ARG_STATUS_CODE_IDOL"

        fun newInstance() = TabHistoryIdolFragment()
    }
}
