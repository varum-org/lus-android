package com.vtnd.lus.data.di

import com.vtnd.lus.data.RepoRepository
import com.vtnd.lus.data.TokenRepository
import com.vtnd.lus.data.UserRepository
import com.vtnd.lus.data.repository.RepoRepositoryImpl
import com.vtnd.lus.data.repository.TokenRepositoryImpl
import com.vtnd.lus.data.repository.UserRepositoryImpl
import com.vtnd.lus.shared.constants.Constants
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {

    single<UserRepository> {
        UserRepositoryImpl(
            local = get(),
            remote = get(),
            tokenLocal = get(),
            repoLocal = get(),
            tokenRepository = get()
        )
    }
    single<RepoRepository> {
        RepoRepositoryImpl(
            local = get(),
            remote = get(),
            application = get(),
            geocoderApiKey = get(named(Constants.KEY_GEOCODE_API)),
            geocoderErrorResponseJsonAdapter = get()
        )
    }
    single<TokenRepository> {
        TokenRepositoryImpl(
            local = get()
//            remote = get()
        )
    }
}
