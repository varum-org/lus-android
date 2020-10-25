package com.vtnd.lus.shared.scheduler.dispatcher

import kotlinx.coroutines.Dispatchers

class DefaultDispatcher : DispatchersProvider {
    override fun dispatcher() = Dispatchers.Default
}
