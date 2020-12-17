package com.vtnd.lus.ui.main.container.history.tab

import android.view.LayoutInflater
import com.vtnd.lus.R
import com.vtnd.lus.base.BaseFragment
import com.vtnd.lus.base.ItemViewHolder
import com.vtnd.lus.databinding.FragmentTabHistoryBinding
import com.vtnd.lus.shared.extensions.showNotificationAlertDialog
import com.vtnd.lus.shared.liveData.observeLiveData
import com.vtnd.lus.shared.type.HistoryActionType
import com.vtnd.lus.ui.main.container.history.adapter.HistoryAdapter
import kotlinx.android.synthetic.main.fragment_tab_history.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class TabHistoryFragment : BaseFragment<FragmentTabHistoryBinding, TabHistoryViewModel>() {
    private lateinit var historyAdapter: HistoryAdapter
    override val viewModel: TabHistoryViewModel by viewModel()

    override fun inflateViewBinding(inflater: LayoutInflater) =
        FragmentTabHistoryBinding.inflate(inflater)

    override fun initialize() {
        arguments?.takeIf { it.containsKey(ARG_STATUS_CODE) }?.apply {
            viewModel.getOder(getInt(ARG_STATUS_CODE))
            historyAdapter = HistoryAdapter(getInt(ARG_STATUS_CODE)) { type, order ->
                when (type) {
                    HistoryActionType.CANCEL -> {
                        viewModel.deleteOrder(order.id!!)
                    }
                }
            }
        }
        tabHistoryRecyclerView.apply {
            adapter = historyAdapter
        }
    }

    override fun registerLiveData() = with(viewModel) {
        super.registerLiveData()
        historys.observeLiveData(viewLifecycleOwner) {
            historyAdapter.submitList(it.map { oder ->
                ItemViewHolder(oder)
            })
        }
        newOrder.observeLiveData(viewLifecycleOwner) {
            showNotificationAlertDialog {
                icon(R.drawable.ic_check)
                statusMessage(getString(R.string.success))
                button(getString(R.string.ok2)) {
                    arguments?.takeIf { it.containsKey(ARG_STATUS_CODE) }?.apply {
                        viewModel.getOder(getInt(ARG_STATUS_CODE))
                    }
                }
            }
        }
    }

    companion object {
        const val ARG_STATUS_CODE = "ARG_STATUS_CODE"

        fun newInstance() = TabHistoryFragment()
    }
}
