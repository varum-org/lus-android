package com.vtnd.lus.base

import com.vtnd.lus.shared.RecyclerViewItem
import com.vtnd.lus.shared.TYPE_ITEM

data class ItemViewHolder<T>(
    val itemData: T,
    override var type: Int = TYPE_ITEM
) : RecyclerViewItem
