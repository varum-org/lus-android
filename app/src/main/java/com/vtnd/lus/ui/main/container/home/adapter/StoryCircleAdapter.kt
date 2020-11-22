package com.vtnd.lus.ui.main.container.home.adapter

import android.view.View
import android.view.ViewGroup
import com.vtnd.lus.R
import com.vtnd.lus.base.ItemViewHolder
import com.vtnd.lus.shared.*

class StoryCircleAdapter(private val onItemClickListener: (Any) -> Unit) :
    BaseAdapter(DIFF_CALLBACK) {

    override fun customViewHolder(parent: ViewGroup, viewType: Int) =
        when (viewType) {
            TYPE_HEADER -> ViewHolder(inflateView(R.layout.item_add_story_cricle, parent))
            TYPE_ITEM -> ViewHolder(inflateView(R.layout.item_story_cricle, parent))
            else -> Any()
        }

    inner class ViewHolder(
        itemView: View
    ) : BaseViewHolder<ItemViewHolder<Any>>(itemView)

    companion object {
        private val DIFF_CALLBACK = object : BaseDiffUtil<ItemViewHolder<Any>>() {
            override fun areItemsTheSame(
                oldItem: ItemViewHolder<Any>,
                newItem: ItemViewHolder<Any>
            ) = oldItem.itemData == oldItem.itemData
        }
    }
}
