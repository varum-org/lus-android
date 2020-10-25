package com.vtnd.lus.di

import com.vtnd.lus.shared.scheduler.dispatcher.*
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dispatcherModule = module {
    single<DispatchersProvider>(named(AppDispatchers.DEFAULT)) {
        DefaultDispatcher()
    }
    single<DispatchersProvider>(named(AppDispatchers.MAIN)) {
        MainDispatcher()
    }
    single<DispatchersProvider>(named(AppDispatchers.UNCONFINED)) {
        UnconfinedDispatcher()
    }
    single<DispatchersProvider>(named(AppDispatchers.IO)) {
        IODispatcher()
    }
}
