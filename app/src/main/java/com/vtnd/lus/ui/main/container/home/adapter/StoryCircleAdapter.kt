package com.vtnd.lus.ui.main.container.home.adapter

import android.view.View
import android.view.ViewGroup
import com.vtnd.lus.R
import com.vtnd.lus.base.ItemViewHolder
import com.vtnd.lus.data.repository.source.remote.api.response.IdolResponse
import com.vtnd.lus.di.GlideApp
import com.vtnd.lus.shared.BaseAdapter
import com.vtnd.lus.shared.BaseDiffUtil
import com.vtnd.lus.shared.BaseViewHolder
import com.vtnd.lus.shared.constants.Constants
import com.vtnd.lus.shared.extensions.safeClick
import kotlinx.android.synthetic.main.item_story_cricle.view.storyCircleImage

class StoryCircleAdapter(private val onItemClickListener: ( IdolResponse) -> Unit) :
    BaseAdapter(DIFF_CALLBACK) {

    override fun customViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(inflateView(R.layout.item_story_cricle, parent))


    inner class ViewHolder(
        itemView: View
    ) : BaseViewHolder<ItemViewHolder<IdolResponse>>(itemView) {

        override fun bind(item: ItemViewHolder<IdolResponse>) {
            super.bind(item)
            itemView.apply {
                item.itemData.let { idolRes ->
                    GlideApp.with(storyCircleImage)
                        .load(Constants.BASE_IMAGE_URL + idolRes.idol!!.imageGallery[0])
                        .placeholder(R.color.pink_50)
                        .error(R.color.red_a400)
                        .dontAnimate()
                        .into(storyCircleImage)
                    safeClick {
                        onItemClickListener( idolRes)
                    }
                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : BaseDiffUtil<ItemViewHolder<IdolResponse>>() {
            override fun areItemsTheSame(
                oldItem: ItemViewHolder<IdolResponse>,
                newItem: ItemViewHolder<IdolResponse>
            ) = oldItem.itemData == newItem.itemData
        }
    }
}
