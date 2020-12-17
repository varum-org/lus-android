package com.vtnd.lus.ui.main.container.historyIdol.adapter

import android.view.View
import com.vtnd.lus.R
import com.vtnd.lus.base.ItemViewHolder
import com.vtnd.lus.data.model.Order
import com.vtnd.lus.di.GlideApp
import com.vtnd.lus.shared.BaseViewHolder
import com.vtnd.lus.shared.constants.Constants
import com.vtnd.lus.shared.extensions.setTextDefaultValue
import com.vtnd.lus.shared.extensions.toString
import com.vtnd.lus.shared.type.HistoryActionType
import kotlinx.android.synthetic.main.item_approve_idol.view.*
import kotlinx.android.synthetic.main.item_done_idol.view.*
import kotlinx.android.synthetic.main.item_done_idol.view.image_view
import kotlinx.android.synthetic.main.item_done_idol.view.text_address
import kotlinx.android.synthetic.main.item_done_idol.view.text_name
import kotlinx.android.synthetic.main.item_done_idol.view.text_note
import kotlinx.android.synthetic.main.item_done_idol.view.text_start_time
import kotlinx.android.synthetic.main.item_done_idol.view.text_total_price

class FinishViewHolder(
    private val onItemClickListener: (HistoryActionType, Order) -> Unit,
    itemView: View
) : BaseViewHolder<ItemViewHolder<Order>>(itemView) {
    override fun bind(item: ItemViewHolder<Order>) {
        super.bind(item)
        itemView.apply {
            item.itemData.let {
                text_name.text = it.userName
                text_start_time.text = context.getString(
                    R.string.start_time_output,
                    it.startDate?.toString(context.getString(R.string.format_date_mm_dd_yyyy_hh_mm))
                )
                text_address.setTextDefaultValue( context.getString(R.string.address_output, it.userAddress))
                text_note.setTextDefaultValue(context.getString(R.string.note_output, it.note))
                text_total_price.text =
                    context.getString(R.string.total_output, it.amount.toString())
                GlideApp.with(context)
                    .load(Constants.BASE_IMAGE_URL + it.userImage)
                    .placeholder(R.color.pink_50)
                    .error(R.color.red_a400)
                    .dontAnimate()
                    .into(image_view)
            }
        }
    }
}