package com.vtnd.lus.data.repository

import com.vtnd.lus.data.TokenRepository
import com.vtnd.lus.data.repository.source.TokenDataSource


class TokenRepositoryImpl(private val local: TokenDataSource.Local) :
    TokenRepository {

    override fun getToken() = local.getToken()

    override fun saveToken(token: String) = local.saveToken(token)

    override fun clearToken() = local.clearToken()
}
