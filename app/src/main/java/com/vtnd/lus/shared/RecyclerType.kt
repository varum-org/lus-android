package com.vtnd.lus.shared

import androidx.annotation.IntDef

@IntDef(
    TYPE_UNKNOWN,
    TYPE_HEADER,
    TYPE_ITEM,
    TYPE_FOOTER
)
@Retention(AnnotationRetention.SOURCE)
annotation class RecyclerType

const val TYPE_UNKNOWN = -1
const val TYPE_HEADER = 0
const val TYPE_ITEM = 1
const val TYPE_FOOTER = 2
