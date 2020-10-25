package com.vtnd.lus.shared.extensions

import android.widget.TextView
import com.vtnd.lus.R
import java.util.*

fun TextView.setTextDefaultValue(value: String?) {
    text = if (value.isNullOrBlank()) context.getString(R.string.no_data_available) else value
}

fun TextView.goneIfTextEmpty() =
    if (text.isNullOrEmpty()) gone() else visible()

fun TextView.setTextAndColor(
    value: String?,
    colorId: Int
) = apply {
    val color = resources.getColor(colorId, context?.theme)
    text = value
    setTextColor(color)
}

fun TextView.setTextDefaultValue(value: Any?, formatDate: ((Date?) -> String?)?) {
    text = if ((value as String?) == "N/A") value
    else formatDate?.invoke(value as Date?)
}
