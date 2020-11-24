package com.vtnd.lus.shared.extensions

import android.content.Context
import com.vtnd.lus.R
import com.vtnd.lus.shared.constants.Constants.EMPTY_DATE
import com.vtnd.lus.shared.constants.Constants.EMPTY_HOURS
import com.vtnd.lus.shared.constants.Constants.FORMAT_HH_MM
import com.vtnd.lus.shared.constants.Constants.FORMAT_MINUTES
import java.text.SimpleDateFormat
import java.util.*

fun Date.toString(formatDate: String, zone: TimeZone = TimeZone.getDefault()): String? =
    SimpleDateFormat(formatDate, Locale.getDefault())
        .apply { timeZone = zone }
        .format(this)

fun Date.toStringDefaultHours(zone: TimeZone = TimeZone.getDefault()) =
    SimpleDateFormat(FORMAT_HH_MM, Locale.getDefault())
        .apply { timeZone = zone }
        .format(this) ?: EMPTY_HOURS

fun Date.toStringDefaultDate(context: Context, zone: TimeZone = TimeZone.getDefault()) =
    SimpleDateFormat(context.getString(R.string.format_date_mm_dd_yyyy), Locale.getDefault())
        .apply { timeZone = zone }
        .format(this) ?: EMPTY_DATE

fun getMinutesDifference(timeFrom: Long?, timeTo: Long?) =
    if (timeFrom != null && timeTo != null)
        (timeTo.minus(timeFrom)).div(FORMAT_MINUTES) else 0

fun Date.getAge(): String? {
    val dob = Calendar.getInstance()
    val today = Calendar.getInstance()
    dob.time = this
    var age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR)
    if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
        age--
    }
    return age.toString()
}