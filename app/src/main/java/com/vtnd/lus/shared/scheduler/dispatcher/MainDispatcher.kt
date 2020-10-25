package com.vtnd.lus.shared.scheduler.dispatcher

import com.vtnd.lus.shared.scheduler.dispatcher.DispatchersProvider
import kotlinx.coroutines.Dispatchers

class MainDispatcher : DispatchersProvider {
    override fun dispatcher() = Dispatchers.Main
}
