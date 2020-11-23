package com.vtnd.lus.ui.main.container.home.adapter

import android.view.View
import android.view.ViewGroup
import com.vtnd.lus.R
import com.vtnd.lus.base.ItemViewHolder
import com.vtnd.lus.data.model.User
import com.vtnd.lus.data.repository.source.remote.api.response.IdolResponse
import com.vtnd.lus.shared.BaseAdapter
import com.vtnd.lus.shared.BaseDiffUtil
import com.vtnd.lus.shared.BaseViewHolder
import com.vtnd.lus.shared.extensions.safeClick
import kotlinx.android.synthetic.main.item_hot_idol.view.*
import java.util.*

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
                    idolRes.user?.birthday?.let {
                        context.getString(R.string.nick_name, idolRes.idol.nickName, it.getAge())
                    } ?: idolRes.idol.nickName
                    idolNameText.text = idolRes.idol.nickName
                    idolLocationText.text =
                        context?.getString(R.string.live_in, "Đà Nẵng")
                    idolAddressText.text =
                        context?.getString(R.string.idol_address, idolRes.idol.address)
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

private fun Date.getAge(): String? {
    val dob = Calendar.getInstance()
    val today = Calendar.getInstance()
    dob.time = this
    var age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR)
    if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
        age--
    }
    return age.toString()
}
