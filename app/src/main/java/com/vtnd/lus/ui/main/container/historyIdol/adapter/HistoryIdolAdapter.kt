package com.vtnd.lus.ui.main.container.historyIdol.adapter

import android.view.ViewGroup
import com.vtnd.lus.R
import com.vtnd.lus.base.ItemViewHolder
import com.vtnd.lus.data.model.Order
import com.vtnd.lus.shared.BaseAdapter
import com.vtnd.lus.shared.BaseDiffUtil
import com.vtnd.lus.shared.type.HistoryActionType
import com.vtnd.lus.shared.type.HistoryType

class HistoryIdolAdapter(
    private val status: Int,
    private val onItemClickListener: (HistoryActionType, Order) -> Unit
) :
    BaseAdapter(DIFF_CALLBACK) {

    override fun customViewHolder(parent: ViewGroup, viewType: Int) =
        when (status) {
            HistoryType.PENDING.statusCode ->
                PendingViewHolder(onItemClickListener,inflateView(R.layout.item_pending_idol, parent))
            HistoryType.APPROVE.statusCode ->
                ApproveViewHolder(onItemClickListener,inflateView(R.layout.item_approve_idol, parent))
            HistoryType.REJECT.statusCode ->
                RejectViewHolder(onItemClickListener,inflateView(R.layout.item_reject_idol, parent))
            HistoryType.FINISH.statusCode ->
                FinishViewHolder(onItemClickListener,inflateView(R.layout.item_done_idol, parent))
            else -> Any()
        }

    companion object {
        private val DIFF_CALLBACK = object : BaseDiffUtil<ItemViewHolder<Order>>() {
            override fun areItemsTheSame(
                oldItem: ItemViewHolder<Order>,
                newItem: ItemViewHolder<Order>
            ) = oldItem.itemData == newItem.itemData
        }
    }
}
