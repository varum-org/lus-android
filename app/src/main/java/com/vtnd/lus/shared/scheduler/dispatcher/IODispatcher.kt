package com.vtnd.lus.shared.scheduler.dispatcher

import com.vtnd.lus.shared.scheduler.dispatcher.DispatchersProvider
import kotlinx.coroutines.Dispatchers

class IODispatcher : DispatchersProvider {
    override fun dispatcher() = Dispatchers.IO
}
