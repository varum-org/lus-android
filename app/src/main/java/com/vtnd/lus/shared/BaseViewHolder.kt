package com.vtnd.lus.shared

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView

open class BaseViewHolder<T : RecyclerViewItem>(itemView: View) : RecyclerView.ViewHolder(
    itemView
) {

    protected val context: Context? = itemView.context

    protected var item: T? = null
        private set

    protected var isSelected = false

    open fun bind(item: T) {
        this.item = item
    }

    open fun bind(item: T, isSelected: Boolean) {
        this.item = item
        this.isSelected = isSelected
    }
}
