package com.vtnd.lus.ui.main.container.profile.adapter

import android.view.View
import android.view.ViewGroup
import com.vtnd.lus.R
import com.vtnd.lus.base.ItemViewHolder
import com.vtnd.lus.di.GlideApp
import com.vtnd.lus.shared.BaseAdapter
import com.vtnd.lus.shared.BaseDiffUtil
import com.vtnd.lus.shared.BaseViewHolder
import com.vtnd.lus.shared.constants.Constants
import kotlinx.android.synthetic.main.item_menu_idol.view.*

class MenuIdolAdapter(private val onItemClickListener: (ItemMenu) -> Unit) :
    BaseAdapter(DIFF_CALLBACK) {

    override fun customViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(inflateView(R.layout.item_menu_idol, parent))

    inner class ViewHolder(
        itemView: View
    ) : BaseViewHolder<ItemViewHolder<ItemMenu>>(itemView) {
        override fun bind(item: ItemViewHolder<ItemMenu>) {
            super.bind(item)
            itemView.apply {
                item.itemData.let {
                    it.nickname?.let { nickname -> titleText.text = nickname }
                    it.avatar?.let { avatar ->
                        GlideApp.with(idolImage)
                            .load(Constants.BASE_IMAGE_URL + avatar)
                            .placeholder(R.color.pink_50)
                            .error(R.color.red_a400)
                            .dontAnimate()
                            .into(idolImage)

                    }
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
