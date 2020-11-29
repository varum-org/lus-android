package com.vtnd.lus.ui.main.container.idolDetail.adapter

import com.vtnd.lus.data.model.Service
import com.vtnd.lus.shared.RecyclerViewItem
import com.vtnd.lus.shared.TYPE_ITEM

data class ItemCard(
    val service: Service,
    val hours: Int = 1,
    override var type: Int = TYPE_ITEM
) : RecyclerViewItem
