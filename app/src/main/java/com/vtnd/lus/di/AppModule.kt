package com.vtnd.lus.di

import com.vtnd.lus.BuildConfig
import com.vtnd.lus.shared.constants.Constants
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {
    single(named(Constants.KEY_BASE_URL)) {
        BuildConfig.BASE_URL
    }
}
