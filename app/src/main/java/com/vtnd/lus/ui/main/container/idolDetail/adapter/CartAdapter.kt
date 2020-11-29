package com.vtnd.lus.ui.main.container.idolDetail.adapter

import android.view.View
import android.view.ViewGroup
import com.vtnd.lus.R
import com.vtnd.lus.shared.BaseAdapter
import com.vtnd.lus.shared.BaseDiffUtil
import com.vtnd.lus.shared.BaseViewHolder
import com.vtnd.lus.shared.type.CardActionType
import com.vtnd.lus.shared.type.CardActionType.MINUS
import com.vtnd.lus.shared.type.CardActionType.PLUS
import kotlinx.android.synthetic.main.item_cart.view.*

class CartAdapter(private val onItemClickListener: (CardActionType, ItemCard) -> Unit) :
    BaseAdapter(DIFF_CALLBACK) {

    override fun customViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(inflateView(R.layout.item_cart, parent))

    inner class ViewHolder(itemView: View) : BaseViewHolder<ItemCard>(itemView) {
        override fun bind(item: ItemCard) {
            super.bind(item)
            itemView.apply {
                item.let {
                    cardServiceNameText.text = it.service.serviceName
                    cardHoursText.text = "${it.hours}h"
                    cardPriceText.text =
                        "${it.service.servicePrice} x ${it.hours} = ${
                            it.service.servicePrice?.times(it.hours)
                        }"
                    cardMinusImage.setOnClickListener { _ ->
                        onItemClickListener.invoke(MINUS, it)
                    }
                    cardPlusImage.setOnClickListener { _ ->
                        onItemClickListener.invoke(PLUS, it)
                    }
                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : BaseDiffUtil<ItemCard>() {
            override fun areItemsTheSame(
                oldItem: ItemCard,
                newItem: ItemCard
            ) = oldItem.service.serviceCode == newItem.service.serviceCode
        }
    }
}
