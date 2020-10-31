package com.vtnd.lus.ui.intro.adapter

import android.view.View
import android.view.ViewGroup
import com.vtnd.lus.R
import com.vtnd.lus.base.ItemViewHolder
import com.vtnd.lus.shared.BaseAdapter
import com.vtnd.lus.shared.BaseDiffUtil
import com.vtnd.lus.shared.BaseViewHolder
import kotlinx.android.synthetic.main.item_intro_slider.view.*

class IntroSlideAdapter : BaseAdapter(DIFF_CALLBACK) {

    override fun customViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(inflateView(R.layout.item_intro_slider, parent))

    inner class ViewHolder(
        itemView: View
    ) : BaseViewHolder<ItemViewHolder<IntroSlide>>(itemView) {

        override fun bind(item: ItemViewHolder<IntroSlide>) {
            itemView.apply {
                item.itemData.let {
                    titleText.text = it.title
                    descriptionText.text = it.description
                    iconImage.setImageResource(it.icon)
                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : BaseDiffUtil<ItemViewHolder<IntroSlide>>() {
            override fun areItemsTheSame(
                oldItem: ItemViewHolder<IntroSlide>,
                newItem: ItemViewHolder<IntroSlide>
            ) = oldItem.itemData.title == oldItem.itemData.title
        }
    }
}
