package com.vtnd.lus.ui.main.container.profile.adapter

import android.view.View
import android.view.ViewGroup
import com.vtnd.lus.R
import com.vtnd.lus.base.ItemViewHolder
import com.vtnd.lus.shared.BaseAdapter
import com.vtnd.lus.shared.BaseDiffUtil
import com.vtnd.lus.shared.BaseViewHolder
import kotlinx.android.synthetic.main.item_menu_setting.view.*

class MenuSettingAdapter(private val onItemClickListener: (ItemMenu) -> Unit) :
    BaseAdapter(DIFF_CALLBACK) {

    override fun customViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(inflateView(R.layout.item_menu_setting, parent))

    inner class ViewHolder(
        itemView: View
    ) : BaseViewHolder<ItemViewHolder<ItemMenu>>(itemView) {
        override fun bind(item: ItemViewHolder<ItemMenu>) {
            super.bind(item)
            itemView.apply {
                item.itemData.let {
                    it.title?.let { title -> titleText.text = context.getString(title) }
                    it.icon?.let { icon -> iconImage.setImageResource(icon) }
                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : BaseDiffUtil<ItemViewHolder<ItemMenu>>() {
            override fun areItemsTheSame(
                oldItem: ItemViewHolder<ItemMenu>,
                newItem: ItemViewHolder<ItemMenu>
            ) = oldItem.itemData == newItem.itemData
        }
    }
}
