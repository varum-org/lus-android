package com.vtnd.lus.data

import com.vtnd.lus.data.model.User
import com.vtnd.lus.data.repository.source.remote.api.request.IdolResponse
import com.vtnd.lus.data.repository.source.remote.api.request.SignUpRequest
import com.vtnd.lus.data.repository.source.remote.api.request.VerifyRequest
import com.vtnd.lus.shared.scheduler.DataResult
import com.vtnd.lus.shared.type.CategoryIdolType
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    //Local
    suspend fun user(): User?

    fun userObservable(): Flow<User?>

    suspend fun setLogin(): DataResult<Unit>

    suspend fun isLogin(): DataResult<String?>

    suspend fun clearLogin(): DataResult<Unit>

    //Remote
    //User
    suspend fun signIn(email: String, password: String): DataResult<Any>

    suspend fun signUp(signUpRequest: SignUpRequest): DataResult<Any>

    suspend fun verifyAccount(verifyRequest: VerifyRequest): DataResult<Any>

    suspend fun getUser(id: String): DataResult<Unit>

    //Idol
    suspend fun getIdols(
        isLogin: Boolean,
        category: CategoryIdolType
    ): DataResult<List<IdolResponse>>
}
