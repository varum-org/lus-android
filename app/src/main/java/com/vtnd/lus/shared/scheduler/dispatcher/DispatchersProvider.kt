package com.vtnd.lus.shared.scheduler.dispatcher

import kotlin.coroutines.CoroutineContext

interface DispatchersProvider {
    fun dispatcher(): CoroutineContext
}
