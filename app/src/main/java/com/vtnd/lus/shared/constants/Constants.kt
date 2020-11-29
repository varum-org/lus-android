package com.vtnd.lus.shared.constants

import java.util.*

/**
 * All constants value should be here and separated if needed.
 */
object Constants {

    // App constants
    const val KEY_BASE_URL = "KEY_BASE_URL"
    const val BASE_IMAGE_URL = "https://res.cloudinary.com/employeern/image/upload/"

    // Format constants
    const val FORMAT_YYY_MM_DD = "yyyy-MM-dd"
    const val FORMAT_DD_MM_YYYY = "dd/MM/yyyy"
    const val FORMAT_YYYY_M = "yyyy/MM"
    const val FORMAT_M_YYYY = "MM/yyyy"
    const val FORMAT_HH_MM = "HH:mm"
    const val ACTUAL_MONTH = 1
    const val EMPTY_HOURS = "--:--"
    const val EMPTY_DATE = "--/--/----"
    const val EMPTY_MONTH = "--/----"
    private const val TIME_ZONE_ID = "UTC"
    val TIME_ZONE_UTC = TimeZone.getTimeZone(TIME_ZONE_ID)
    const val FORMAT_MINUTES = 60000
    const val DEFAULT_PAGE = 1
    const val SYMBOL_SEPARATOR = " ● "
}
