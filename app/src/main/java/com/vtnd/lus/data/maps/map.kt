package com.vtnd.lus.data.maps

import android.location.Location
import com.vtnd.lus.ui.main.container.registerIdol.addressIdol.DomainLocation

fun Location.toLocationDomain(): DomainLocation {
    return DomainLocation(
        latitude = latitude,
        longitude = longitude
    )
}
