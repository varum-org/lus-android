package com.vtnd.lus.ui.main.container.registerIdol.serviceIdol.adapter

import android.view.View
import android.view.ViewGroup
import com.vtnd.lus.R
import com.vtnd.lus.base.ItemViewHolder
import com.vtnd.lus.data.model.Service
import com.vtnd.lus.di.GlideApp
import com.vtnd.lus.shared.BaseAdapter
import com.vtnd.lus.shared.BaseDiffUtil
import com.vtnd.lus.shared.BaseViewHolder
import com.vtnd.lus.shared.extensions.safeClick
import kotlinx.android.synthetic.main.item_add_service.view.*

class AddServiceAdapter(
    private val onItemClickListener: (Service) -> Unit,
    private val onDeleteItemClickListener: (Service) -> Unit
) : BaseAdapter(DIFF_CALLBACK) {

    override fun customViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(inflateView(R.layout.item_add_service, parent))

    inner class ViewHolder(
        itemView: View
    ) : BaseViewHolder<ItemViewHolder<Service>>(itemView) {

        override fun bind(item: ItemViewHolder<Service>) {
            itemView.apply {
                item.itemData.let {
                    serviceNameText.text = it.serviceName
                    serviceDescriptionText.text = it.serviceDescription
                    servicePrice.text = it.servicePrice.toString()
                    GlideApp.with(serviceIconImage)
                        .load(it.serviceImagePath)
                        .placeholder(R.color.pink_50)
                        .error(R.color.red_a400)
                        .dontAnimate()
                        .into(serviceIconImage)
                    serviceActionButton.safeClick { _ ->
                        onDeleteItemClickListener.invoke(it)
                    }
                    safeClick { _ ->
                        onItemClickListener.invoke(it)
                    }
                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : BaseDiffUtil<ItemViewHolder<Service>>() {
            override fun areItemsTheSame(
                oldItem: ItemViewHolder<Service>,
                newItem: ItemViewHolder<Service>
            ) = oldItem.itemData.serviceCode == newItem.itemData.serviceCode
        }
    }
}
