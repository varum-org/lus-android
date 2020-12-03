package com.vtnd.lus.ui.main.container.message.adapter

import android.view.View
import android.view.ViewGroup
import com.vtnd.lus.R
import com.vtnd.lus.base.ItemViewHolder
import com.vtnd.lus.data.model.Message
import com.vtnd.lus.shared.BaseAdapter
import com.vtnd.lus.shared.BaseDiffUtil
import com.vtnd.lus.shared.BaseViewHolder
import com.vtnd.lus.shared.type.MessageType
import kotlinx.android.synthetic.main.item_chat_mine.view.*
import kotlinx.android.synthetic.main.item_chat_partner.view.*

class MessageAdapter(private val onItemClickListener: (Message) -> Unit) :
    BaseAdapter(DIFF_CALLBACK) {

    override fun customViewHolder(parent: ViewGroup, viewType: Int) =
        when (viewType) {
            MessageType.CHAT_PARTNER.type ->
                PartnerViewHolder(
                    inflateView(R.layout.item_chat_partner, parent)
                )
            MessageType.CHAT_MINE.type ->
                MineViewHolder(inflateView(R.layout.item_chat_mine, parent))
            else -> Any()
        }


    inner class PartnerViewHolder(itemView: View) :
        BaseViewHolder<ItemViewHolder<Message>>(itemView) {
        override fun bind(item: ItemViewHolder<Message>) {
            super.bind(item)
            itemView.apply {
                item.itemData.let {
                    messagePartner.text = it.content
                }
            }
        }
    }

    inner class MineViewHolder(itemView: View) : BaseViewHolder<ItemViewHolder<Message>>(itemView) {
        override fun bind(item: ItemViewHolder<Message>) {
            super.bind(item)
            itemView.apply {
                item.itemData.let {
                    messageMine.text = it.content
                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : BaseDiffUtil<ItemViewHolder<Message>>() {
            override fun areItemsTheSame(
                oldItem: ItemViewHolder<Message>,
                newItem: ItemViewHolder<Message>
            ) = oldItem.itemData == newItem.itemData
        }
    }
}
