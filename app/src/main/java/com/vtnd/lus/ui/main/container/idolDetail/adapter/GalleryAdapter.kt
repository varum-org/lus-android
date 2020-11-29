package com.vtnd.lus.ui.main.container.idolDetail.adapter

import android.view.View
import android.view.ViewGroup
import com.vtnd.lus.R
import com.vtnd.lus.base.ItemViewHolder
import com.vtnd.lus.di.GlideApp
import com.vtnd.lus.shared.BaseAdapter
import com.vtnd.lus.shared.BaseDiffUtil
import com.vtnd.lus.shared.BaseViewHolder
import com.vtnd.lus.shared.constants.Constants.BASE_IMAGE_URL
import kotlinx.android.synthetic.main.item_gallery.view.*

class GalleryAdapter(private val onItemClickListener: (String) -> Unit) :
    BaseAdapter(DIFF_CALLBACK) {

    override fun customViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(inflateView(R.layout.item_gallery, parent))

    inner class ViewHolder(
        itemView: View
    ) : BaseViewHolder<ItemViewHolder<String>>(itemView) {

        override fun bind(item: ItemViewHolder<String>) {
            itemView.apply {
                item.itemData.let {
                    GlideApp.with(this)
                        .load(BASE_IMAGE_URL + it)
                        .placeholder(R.color.pink_50)
                        .error(R.color.red_a400)
                        .dontAnimate()
                        .into(idolImage)
                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK =
            object : BaseDiffUtil<ItemViewHolder<String>>() {
                override fun areItemsTheSame(
                    oldItem: ItemViewHolder<String>,
                    newItem: ItemViewHolder<String>
                ) = oldItem.itemData == newItem.itemData
            }
    }
}
