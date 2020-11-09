package com.vtnd.lus.data.repository

import com.vtnd.lus.base.BaseRepository
import com.vtnd.lus.data.UserRepository
import com.vtnd.lus.data.repository.source.UserDataSource
import com.vtnd.lus.data.repository.source.remote.api.request.SignInRequest

class UserRepositoryImpl(
    private val remote: UserDataSource.Remote,
    private val local: UserDataSource.Local
) : BaseRepository(), UserRepository {

    override suspend fun signIn(email: String, password: String) =
        withResultContext {
            remote.signIn(email,password).data
        }
}
