package com.vtnd.lus.data.di

import com.vtnd.lus.data.RepoRepository
import com.vtnd.lus.data.TokenRepository
import com.vtnd.lus.data.UserRepository
import com.vtnd.lus.data.repository.RepoRepositoryImpl
import com.vtnd.lus.data.repository.TokenRepositoryImpl
import com.vtnd.lus.data.repository.UserRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {

    single<UserRepository> {
        UserRepositoryImpl(
            local = get(),
            remote = get(),
            tokenLocal = get(),
            repoLocal = get()
        )
    }
    single<RepoRepository> {
        RepoRepositoryImpl(
            local = get(),
            remote = get()
        )
    }
    single<TokenRepository> {
        TokenRepositoryImpl(
            local = get()
//            remote = get()
        )
    }
}
