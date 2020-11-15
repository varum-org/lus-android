package com.vtnd.lus.data.repository.source

import com.vtnd.lus.data.repository.source.remote.api.request.SignUpRequest
import com.vtnd.lus.data.repository.source.remote.api.request.VerifyRequest
import com.vtnd.lus.data.repository.source.remote.api.response.BaseResponse
import com.vtnd.lus.data.repository.source.remote.api.response.SignInResponse

interface UserDataSource {

    interface Local

    interface Remote {
        suspend fun signIn(email: String, password: String): BaseResponse<SignInResponse>

        suspend fun signUp(signUpRequest: SignUpRequest): BaseResponse<Any>

        suspend fun verifyAccount(verifyRequest: VerifyRequest): BaseResponse<Any>
    }
}
