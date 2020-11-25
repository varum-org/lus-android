package com.vtnd.lus.ui.main.container.idolDetail.adapter

import android.view.View
import android.view.ViewGroup
import com.vtnd.lus.R
import com.vtnd.lus.base.ItemViewHolder
import com.vtnd.lus.shared.BaseAdapter
import com.vtnd.lus.shared.BaseDiffUtil
import com.vtnd.lus.shared.BaseViewHolder

class GalleryAdapter(private val onItemClickListener: (Any) -> Unit) :
    BaseAdapter(DIFF_CALLBACK) {

    override fun customViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(inflateView(R.layout.item_gallery, parent))

    inner class ViewHolder(
        itemView: View
    ) : BaseViewHolder<ItemViewHolder<Any>>(itemView) {

        override fun bind(item: ItemViewHolder<Any>) {
            itemView.apply {
                item.itemData.let {
                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK =
            object : BaseDiffUtil<ItemViewHolder<Any>>() {
                override fun areItemsTheSame(
                    oldItem: ItemViewHolder<Any>,
                    newItem: ItemViewHolder<Any>
                ) = oldItem.itemData == oldItem.itemData
            }
    }
}
