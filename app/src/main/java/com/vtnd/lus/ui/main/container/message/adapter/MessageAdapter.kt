package com.vtnd.lus.ui.main.container.message.adapter

import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.core.content.ContextCompat
import com.agrawalsuneet.dotsloader.loaders.LazyLoader
import com.vtnd.lus.R
import com.vtnd.lus.base.ItemViewHolder
import com.vtnd.lus.data.model.Message
import com.vtnd.lus.shared.BaseAdapter
import com.vtnd.lus.shared.BaseDiffUtil
import com.vtnd.lus.shared.BaseViewHolder
import com.vtnd.lus.shared.extensions.gone
import com.vtnd.lus.shared.extensions.visible
import com.vtnd.lus.shared.type.MessageType
import kotlinx.android.synthetic.main.item_chat_mine.view.*
import kotlinx.android.synthetic.main.item_chat_partner.view.*
import kotlinx.android.synthetic.main.item_chat_typing.view.*

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
            MessageType.TYPING.type ->
                TypingViewHolder(inflateView(R.layout.item_chat_typing, parent))
            else -> Any()
        }


    inner class PartnerViewHolder(itemView: View) :
        BaseViewHolder<ItemViewHolder<Message>>(itemView) {
        override fun bind(item: ItemViewHolder<Message>) {
            super.bind(item)
            itemView.apply {
                item.itemData.let {
                    messagePartner.text = it.content
                    usernamePartner.text = it.userId
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
                    if (it.isRead == 1)
                        isReadText.visible()
                    else isReadText.gone()
                }
            }
        }
    }

    inner class TypingViewHolder(itemView: View) :
        BaseViewHolder<ItemViewHolder<Message>>(itemView) {
        override fun bind(item: ItemViewHolder<Message>) {
            super.bind(item)
            itemView.typingLayout.apply {
                removeAllViews()
                val lazyLoader = LazyLoader(
                    context, resources.getDimension(R.dimen.dp_3).toInt(), 15,
                    ContextCompat.getColor(context, R.color.grey_600),
                    ContextCompat.getColor(context, R.color.grey_600),
                    ContextCompat.getColor(context, R.color.grey_600)
                ).apply {
                    animDuration = 200
                    firstDelayDuration = 100
                    secondDelayDuration = 200
                    interpolator = DecelerateInterpolator()
                }
                addView(lazyLoader)
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
