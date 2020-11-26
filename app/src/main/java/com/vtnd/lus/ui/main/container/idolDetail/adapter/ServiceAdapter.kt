package com.vtnd.lus.ui.main.container.idolDetail.adapter

import android.view.View
import android.view.ViewGroup
import com.vtnd.lus.R
import com.vtnd.lus.base.ItemViewHolder
import com.vtnd.lus.data.model.Service
import com.vtnd.lus.shared.BaseAdapter
import com.vtnd.lus.shared.BaseDiffUtil
import com.vtnd.lus.shared.BaseViewHolder
import kotlinx.android.synthetic.main.item_service.view.*

class ServiceAdapter(private val onItemClickListener: (Service) -> Unit) :
    BaseAdapter(DIFF_CALLBACK) {

    override fun customViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(inflateView(R.layout.item_service, parent))

    inner class ViewHolder(
        itemView: View
    ) : BaseViewHolder<ItemViewHolder<Service>>(itemView) {

        override fun bind(item: ItemViewHolder<Service>) {
            itemView.apply {
                item.itemData.let {
                    serviceNameText.text = it.serviceName
                    serviceDescriptionText.text = it.serviceDescription
                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK =
            object : BaseDiffUtil<ItemViewHolder<Service>>() {
                override fun areItemsTheSame(
                    oldItem: ItemViewHolder<Service>,
                    newItem: ItemViewHolder<Service>
                ) = oldItem.itemData == oldItem.itemData
            }
    }
}
