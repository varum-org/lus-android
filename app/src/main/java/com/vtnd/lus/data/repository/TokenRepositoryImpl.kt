package com.vtnd.lus.data.repository

import com.vtnd.lus.data.TokenRepository
import com.vtnd.lus.data.repository.source.TokenDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.distinctUntilChanged
import timber.log.Timber

class TokenRepositoryImpl(private val local: TokenDataSource.Local) : TokenRepository {

    override fun getToken() = local.getToken()

    override fun clearToken() = local.clearToken()

    @ExperimentalCoroutinesApi
    override fun tokenObservable() =
        local.tokenObservable()
            .distinctUntilChanged()
            .buffer(1).let {
                Timber.i("userObservable")
                it
            }
}
