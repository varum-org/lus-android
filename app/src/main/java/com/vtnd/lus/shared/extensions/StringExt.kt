package com.vtnd.lus.shared.extensions

import android.content.Context
import com.vtnd.lus.R
import com.vtnd.lus.shared.constants.Constants.EMPTY_DATE
import com.vtnd.lus.shared.constants.Constants.FORMAT_DD_MM_YYYY
import com.vtnd.lus.shared.constants.Constants.TIME_ZONE_UTC
import java.text.SimpleDateFormat
import java.util.*

fun String.toDate(formatDate: String, zone: TimeZone = TIME_ZONE_UTC): Date? =
    SimpleDateFormat(formatDate, Locale.getDefault())
        .apply { timeZone = zone }
        .parse(this)

fun String.toString(formatToDate: String, formatToString: String) =
    toDate(formatToDate)?.toString(formatToString)

fun String.toStringDefaultDate(
    context: Context,
    formatDate: String = FORMAT_DD_MM_YYYY
) =
    this.toString(
        formatToDate = formatDate,
        formatToString = context.getString(R.string.format_date_mm_dd_yyyy)
    ) ?: EMPTY_DATE

fun <T> String.compare(value: String, ifTrue: T, ifFalse: T) =
    if (this.contains(value)) ifTrue else ifFalse
