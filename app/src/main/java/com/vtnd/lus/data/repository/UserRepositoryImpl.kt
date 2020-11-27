package com.vtnd.lus.data.repository

import com.vtnd.lus.base.BaseRepository
import com.vtnd.lus.data.UserRepository
import com.vtnd.lus.data.repository.source.RepoDataSource
import com.vtnd.lus.data.repository.source.TokenDataSource
import com.vtnd.lus.data.repository.source.UserDataSource
import com.vtnd.lus.data.repository.source.remote.api.request.SignUpRequest
import com.vtnd.lus.data.repository.source.remote.api.request.VerifyRequest
import com.vtnd.lus.data.repository.source.remote.api.response.IdolResponse
import com.vtnd.lus.shared.extensions.toIdolResponses
import com.vtnd.lus.shared.scheduler.DataResult
import com.vtnd.lus.shared.type.CategoryIdolType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.distinctUntilChanged
import timber.log.Timber

class UserRepositoryImpl(
    private val remote: UserDataSource.Remote,
    private val local: UserDataSource.Local,
    private val tokenLocal: TokenDataSource.Local,
    private val repoLocal: RepoDataSource.Local
) : BaseRepository(), UserRepository {

    override suspend fun signIn(email: String, password: String) =
        withResultContext {
            val (token, user) = remote.signIn(email, password).data
            user?.id?.let { id ->
                val (profile) = remote.getUser(id).data
                profile?.let { local.saveUser(profile) }
                token
                    ?.let { tokenLocal.saveToken(it) }
                    .let { local.setLogin() }
            } ?: tokenLocal.clearToken()
        }

    override suspend fun signUp(signUpRequest: SignUpRequest) =
        withResultContext {
            remote.signUp(signUpRequest).data
        }

    override suspend fun verifyAccount(verifyRequest: VerifyRequest) =
        withResultContext {
            remote.verifyAccount(verifyRequest).data
        }

    override suspend fun getUser(id: String) =
        withResultContext {
            val (user) = remote.getUser(id).data
            user?.let { local.saveUser(user) } ?: tokenLocal.clearToken()
        }

    override suspend fun user() = local.user()

    @ExperimentalCoroutinesApi
    override fun userObservable() =
        local.userObservable()
            .distinctUntilChanged()
            .buffer(1).let {
                Timber.i("userObservable")
                it
            }

    override suspend fun setLogin() =
        withResultContext {
            local.setLogin()
        }

    override suspend fun isLogin() =
        withResultContext {
            local.isLogin()
        }

    override suspend fun clearLogin() =
        withResultContext {
            local.clearLogin()
        }

    override suspend fun getIdols(
        isLogin: Boolean,
        category: CategoryIdolType
    ): DataResult<List<IdolResponse>> =
        withResultContext {
            remote.getIdols(isLogin, category).data.toIdolResponses(repoLocal.services())
        }


}

