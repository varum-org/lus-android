package com.vtnd.lus.data.repository

import com.vtnd.lus.base.BaseRepository
import com.vtnd.lus.data.UserRepository
import com.vtnd.lus.data.repository.source.UserDataSource

class UserRepositoryImpl(
    private val remote: UserDataSource.Remote,
    private val local: UserDataSource.Local
) : BaseRepository(), UserRepository {

    override fun getToken() = local.getToken()

    override fun saveToken(token: String) = local.saveToken(token)

    override fun clearToken() = local.clearToken()
}
