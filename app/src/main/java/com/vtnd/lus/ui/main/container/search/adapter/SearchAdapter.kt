package com.vtnd.lus.ui.main.container.search.adapter

import android.view.View
import android.view.ViewGroup
import com.vtnd.lus.R
import com.vtnd.lus.base.ItemViewHolder
import com.vtnd.lus.data.model.Idol
import com.vtnd.lus.di.GlideApp
import com.vtnd.lus.shared.BaseAdapter
import com.vtnd.lus.shared.BaseDiffUtil
import com.vtnd.lus.shared.BaseViewHolder
import com.vtnd.lus.shared.constants.Constants
import com.vtnd.lus.shared.extensions.safeClick
import com.vtnd.lus.shared.extensions.setTint
import kotlinx.android.synthetic.main.item_search.view.*

class SearchAdapter(private val onItemClickListener: (Idol) -> Unit) :
    BaseAdapter(DIFF_CALLBACK) {

    override fun customViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(inflateView(R.layout.item_search, parent))

    inner class ViewHolder(
        itemView: View
    ) : BaseViewHolder<ItemViewHolder<Idol>>(itemView) {
        override fun bind(item: ItemViewHolder<Idol>) {
            super.bind(item)
            itemView.apply {
                if (adapterPosition % 2 == 0) {
                    genderLayout.setTint(R.color.blue_a100)
                    genderImage.setImageResource(R.drawable.ic_man)
                    genderText.text = context?.getString(R.string.male)
                } else {
                    genderLayout.setTint(R.color.red_a100)
                    genderImage.setImageResource(R.drawable.ic_woman)
                    genderText.text = context?.getString(R.string.female)
                }
                item.itemData.let {
                    GlideApp.with(this)
                        .load(Constants.BASE_IMAGE_URL + it.imageGallery[0])
                        .placeholder(R.color.pink_50)
                        .error(R.color.red_a400)
                        .dontAnimate()
                        .into(searchIdolImage)
                    searchIdolImage
                    idolNameText.text = it.nickName
                    addressText.text = it.address
                }
                safeClick {
                    onItemClickListener(item.itemData)
                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : BaseDiffUtil<ItemViewHolder<Idol>>() {
            override fun areItemsTheSame(
                oldItem: ItemViewHolder<Idol>,
                newItem: ItemViewHolder<Idol>
            ) = oldItem.itemData == newItem.itemData
        }
    }
}
