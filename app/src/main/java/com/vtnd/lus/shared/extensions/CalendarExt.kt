package com.vtnd.lus.shared.extensions

import com.vtnd.lus.shared.constants.Constants
import java.util.*

fun Calendar.getMonth() = this.get(Calendar.MONTH) + Constants.ACTUAL_MONTH

fun Calendar.toTimeSheetCalendar(endDate: Int?) = apply {
    if (getCalendar().get(Calendar.MONTH) == get(Calendar.MONTH)) {
        val endDateNotNull = endDate ?: getActualMaximum(Calendar.DAY_OF_MONTH)
        if (get(Calendar.DATE) > endDateNotNull)
            add(Calendar.MONTH, Constants.ACTUAL_MONTH)
    }
}

fun getCalendar(timeZone: TimeZone = TimeZone.getDefault()): Calendar =
    Calendar.getInstance(timeZone)
