package com.vtnd.lus.data.repository

import com.vtnd.lus.base.BaseRepository
import com.vtnd.lus.data.UserRepository
import com.vtnd.lus.data.repository.source.UserDataSource
import com.vtnd.lus.data.repository.source.remote.api.request.SignInRequest
import com.vtnd.lus.data.repository.source.remote.api.request.SignUpRequest
import com.vtnd.lus.shared.scheduler.DataResult

class UserRepositoryImpl(
    private val remote: UserDataSource.Remote,
    private val local: UserDataSource.Local
) : BaseRepository(), UserRepository {

    override suspend fun signIn(email: String, password: String) =
        withResultContext {
            remote.signIn(email,password).data
        }

    override suspend fun signUp(signUpRequest: SignUpRequest)=
        withResultContext {
            remote.signUp(signUpRequest).data
        }
}
