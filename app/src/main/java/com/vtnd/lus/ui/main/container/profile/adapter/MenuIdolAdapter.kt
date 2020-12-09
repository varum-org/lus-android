package com.vtnd.lus.ui.main.container.profile.adapter

import android.view.View
import android.view.ViewGroup
import com.vtnd.lus.R
import com.vtnd.lus.base.ItemViewHolder
import com.vtnd.lus.data.model.Idol
import com.vtnd.lus.di.GlideApp
import com.vtnd.lus.shared.*
import com.vtnd.lus.shared.constants.Constants
import com.vtnd.lus.shared.extensions.safeClick
import kotlinx.android.synthetic.main.item_menu_idol.view.*

class MenuIdolAdapter(private val onItemClickListener: (Any) -> Unit) :
    BaseAdapter(DIFF_CALLBACK) {

    override fun customViewHolder(parent: ViewGroup, viewType: Int) =
        when (viewType) {
            TYPE_ITEM -> ViewHolder(inflateView(R.layout.item_menu_idol, parent))
            TYPE_FOOTER -> AddViewHolder(inflateView(R.layout.item_menu_add_idol, parent))
            else -> Any()
        }

    inner class ViewHolder(
        itemView: View
    ) : BaseViewHolder<ItemViewHolder<Idol>>(itemView) {
        override fun bind(item: ItemViewHolder<Idol>) {
            super.bind(item)
            itemView.apply {
                item.itemData.let {
                    it.nickName?.let { nickname -> titleText.text = nickname }
                    it.imageGallery[0].let { avatar ->
                        GlideApp.with(idolImage)
                            .load(Constants.BASE_IMAGE_URL + avatar)
                            .placeholder(R.color.pink_50)
                            .error(R.color.red_a400)
                            .dontAnimate()
                            .into(idolImage)
                        safeClick { _ ->
                            onItemClickListener.invoke(it)
                        }
                    }
                }
            }
        }
    }

    inner class AddViewHolder(
        itemView: View
    ) : BaseViewHolder<ItemViewHolder<Any>>(itemView) {
        override fun bind(item: ItemViewHolder<Any>) {
            super.bind(item)
            itemView.apply {
                item.itemData.let {
                    safeClick { _ ->
                        onItemClickListener.invoke(it)
                    }
                }
            }
        }
    }


    companion object {
        private val DIFF_CALLBACK = object : BaseDiffUtil<ItemViewHolder<Any>>() {
            override fun areItemsTheSame(
                oldItem: ItemViewHolder<Any>,
                newItem: ItemViewHolder<Any>
            ) = oldItem.itemData == newItem.itemData
        }
    }
}
