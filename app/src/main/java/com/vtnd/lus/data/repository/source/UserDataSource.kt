package com.vtnd.lus.data.repository.source

import com.vtnd.lus.data.model.User
import com.vtnd.lus.data.repository.source.remote.api.response.IdolResponse
import com.vtnd.lus.data.repository.source.remote.api.request.SignUpRequest
import com.vtnd.lus.data.repository.source.remote.api.request.VerifyRequest
import com.vtnd.lus.data.repository.source.remote.api.response.BaseResponse
import com.vtnd.lus.data.repository.source.remote.api.response.SignInResponse
import com.vtnd.lus.data.repository.source.remote.api.response.UserResponse
import com.vtnd.lus.shared.type.CategoryIdolType
import kotlinx.coroutines.flow.Flow

interface UserDataSource {

    interface Local {

        fun userObservable(): Flow<User?>

        suspend fun user(): User?

        suspend fun saveUser(user: User)

        suspend fun setLogin()

        suspend fun isLogin(): String?

        suspend fun clearLogin()
    }

    interface Remote {
        // User

        suspend fun signIn(email: String, password: String): BaseResponse<SignInResponse>

        suspend fun signUp(signUpRequest: SignUpRequest): BaseResponse<Any>

        suspend fun verifyAccount(verifyRequest: VerifyRequest): BaseResponse<Any>

        suspend fun getUser(id: String): BaseResponse<UserResponse>

        //Idol

        suspend fun getIdols(
            isLogin: Boolean,
            category: CategoryIdolType
        ): BaseResponse<List<IdolResponse>>
    }
}
