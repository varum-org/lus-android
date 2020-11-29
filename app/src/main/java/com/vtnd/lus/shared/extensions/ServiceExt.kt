package com.vtnd.lus.shared.extensions

import com.vtnd.lus.data.model.Service
import com.vtnd.lus.data.repository.source.remote.api.response.IdolResponse
import com.vtnd.lus.ui.main.container.idolDetail.adapter.ItemCard

fun List<IdolResponse>.toIdolResponses(services: List<Service>?) =
    this.map {
        it.copy(idol = it.idol.copy(services = it.idol.services.toServices(services)))
    }

fun List<Service>.toServices(services: List<Service>?) =
    this.map {
        it.toService(services)
    }

fun Service.toService(services: List<Service>?) =
    services?.singleOrNull() {
        this.serviceCode == it.serviceCode
    }!!.copy(servicePrice = this.servicePrice)

fun List<ItemCard>.sumPrice(): Int {
    var sum = 0
    for (element in this) {
        sum += element.service.servicePrice!! * element.hours
    }
    return sum
}
