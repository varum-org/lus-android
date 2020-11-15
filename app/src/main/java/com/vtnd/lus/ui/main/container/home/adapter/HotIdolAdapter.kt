package com.vtnd.lus.ui.main.container.home.adapter

import android.view.View
import android.view.ViewGroup
import com.vtnd.lus.R
import com.vtnd.lus.base.ItemViewHolder
import com.vtnd.lus.data.model.User
import com.vtnd.lus.data.repository.source.remote.api.request.IdolResponse
import com.vtnd.lus.shared.BaseAdapter
import com.vtnd.lus.shared.BaseDiffUtil
import com.vtnd.lus.shared.BaseViewHolder
import com.vtnd.lus.shared.extensions.safeClick
import kotlinx.android.synthetic.main.item_hot_idol.view.*

class HotIdolAdapter(private val onItemClickListener: (View, IdolResponse) -> Unit) :
    BaseAdapter(DIFF_CALLBACK) {

    override fun customViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(inflateView(R.layout.item_hot_idol, parent))

    inner class ViewHolder(
        itemView: View
    ) : BaseViewHolder<ItemViewHolder<IdolResponse>>(itemView) {

        override fun bind(item: ItemViewHolder<IdolResponse>) {
            itemView.apply {
                item.itemData.let { idolRes ->
                    idolImage.transitionName = idolRes.idol.id
                    safeClick {
                        onItemClickListener(idolImage, idolRes)
                    }
                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK =
            object : BaseDiffUtil<ItemViewHolder<Pair<User?, IdolResponse?>>>() {
                override fun areItemsTheSame(
                    oldItem: ItemViewHolder<Pair<User?, IdolResponse?>>,
                    newItem: ItemViewHolder<Pair<User?, IdolResponse?>>
                ) = oldItem.itemData.first?.id == oldItem.itemData.first?.id
            }
    }
}
