package com.vtnd.lus.data.di

import com.vtnd.lus.data.UserRepository
import com.vtnd.lus.data.repository.UserRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {

    single<UserRepository> {
        UserRepositoryImpl(
            remote = get(),
            local = get()
        )
    }
}
