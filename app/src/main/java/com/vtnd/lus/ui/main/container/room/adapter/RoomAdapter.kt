package com.vtnd.lus.ui.main.container.room.adapter

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.vtnd.lus.R
import com.vtnd.lus.base.ItemViewHolder
import com.vtnd.lus.data.model.User
import com.vtnd.lus.data.repository.source.remote.api.response.RoomResponse
import com.vtnd.lus.di.GlideApp
import com.vtnd.lus.shared.BaseAdapter
import com.vtnd.lus.shared.BaseDiffUtil
import com.vtnd.lus.shared.BaseViewHolder
import com.vtnd.lus.shared.constants.Constants
import com.vtnd.lus.shared.extensions.dpToPx
import com.vtnd.lus.shared.extensions.randomAvatar
import com.vtnd.lus.shared.extensions.safeClick
import kotlinx.android.synthetic.main.item_room.view.*

class RoomAdapter(
    private val onItemClickListener: (RoomResponse) -> Unit
) : BaseAdapter(DIFF_CALLBACK) {
    lateinit var user: User


    override fun customViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(inflateView(R.layout.item_room, parent))

    inner class ViewHolder(
        itemView: View
    ) : BaseViewHolder<ItemViewHolder<RoomResponse>>(itemView) {

        override fun bind(item: ItemViewHolder<RoomResponse>) {
            super.bind(item)
            itemView.apply {
                item.itemData.let {
                    if (adapterPosition == 0) {
                        setBackgroundColor(resources.getColor(R.color.pink_50, context.theme))
                    }
                    it.userReceive?.let { userNotNull ->
                        nameText.text = userNotNull.userName
                        addressText.text = userNotNull.address
                        userNotNull.imagePath?.let { imagePath ->
                            GlideApp.with(avatarImage)
                                .load(Constants.BASE_IMAGE_URL + imagePath)
                                .placeholder(R.color.pink_50)
                                .error(R.color.red_a400)
                                .dontAnimate()
                                .into(avatarImage)
                        } ?: avatarImage.randomAvatar()
                    }
                    safeClick { _ ->
                        onItemClickListener.invoke(it)
                    }
                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : BaseDiffUtil<ItemViewHolder<RoomResponse>>() {
            override fun areItemsTheSame(
                oldItem: ItemViewHolder<RoomResponse>,
                newItem: ItemViewHolder<RoomResponse>
            ) = oldItem.itemData.room?.id == newItem.itemData.room?.id
        }
    }
}
