package com.vtnd.lus.shared.scheduler.dispatcher

import com.vtnd.lus.shared.scheduler.dispatcher.DispatchersProvider
import kotlinx.coroutines.Dispatchers

class UnconfinedDispatcher : DispatchersProvider {
    override fun dispatcher() = Dispatchers.Unconfined
}
