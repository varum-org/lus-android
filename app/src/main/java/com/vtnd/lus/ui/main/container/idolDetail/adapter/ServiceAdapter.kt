package com.vtnd.lus.ui.main.container.idolDetail.adapter

import android.view.View
import android.view.ViewGroup
import com.vtnd.lus.R
import com.vtnd.lus.di.GlideApp
import com.vtnd.lus.shared.BaseAdapter
import com.vtnd.lus.shared.BaseDiffUtil
import com.vtnd.lus.shared.BaseViewHolder
import com.vtnd.lus.shared.constants.Constants
import com.vtnd.lus.shared.extensions.safeClick
import kotlinx.android.synthetic.main.item_service.view.*

class ServiceAdapter(private val onItemClickListener: (ItemService, Int) -> Unit) :
    BaseAdapter(DIFF_CALLBACK) {

    override fun customViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(inflateView(R.layout.item_service, parent))

    inner class ViewHolder(
        itemView: View
    ) : BaseViewHolder<ItemService>(itemView) {

        override fun bind(item: ItemService) {
            itemView.apply {
                item.service.let {
                    serviceNameText.text = it.serviceName
                    serviceDescriptionText.text = it.serviceDescription
                    servicePrice.text = it.servicePrice.toString()
                    GlideApp.with(serviceIconImage)
                        .load(Constants.BASE_IMAGE_URL + it.serviceImagePath)
                        .placeholder(R.color.pink_50)
                        .error(R.color.red_a400)
                        .dontAnimate()
                        .into(serviceIconImage)
                    serviceActionButton.setBackgroundResource(if (item.selected) R.drawable.ic_negative_circle
                    else R.drawable.ic_plus_circle)
                    safeClick {
                        onItemClickListener.invoke(item, adapterPosition)
                    }
                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : BaseDiffUtil<ItemService>() {
            override fun areItemsTheSame(
                oldItem: ItemService,
                newItem: ItemService
            ) = oldItem.service.serviceCode == newItem.service.serviceCode
        }
    }
}
