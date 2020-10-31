package com.vtnd.lus.data.di

import com.vtnd.lus.data.RepoRepository
import com.vtnd.lus.data.UserRepository
import com.vtnd.lus.data.repository.RepoRepositoryImpl
import com.vtnd.lus.data.repository.UserRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {

    single<UserRepository> {
        UserRepositoryImpl(
            local = get()
//            remote = get()
        )
    }
    single<RepoRepository> {
        RepoRepositoryImpl(
            local = get()
//            remote = get()
        )
    }
}
