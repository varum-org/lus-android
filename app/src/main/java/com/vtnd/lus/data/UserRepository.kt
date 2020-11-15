package com.vtnd.lus.data

import com.vtnd.lus.data.repository.source.remote.api.request.SignUpRequest
import com.vtnd.lus.data.repository.source.remote.api.request.VerifyRequest
import com.vtnd.lus.data.repository.source.remote.api.response.SignInResponse
import com.vtnd.lus.shared.scheduler.DataResult

interface UserRepository {

    //Remote
    suspend fun signIn(email: String, password: String): DataResult<SignInResponse>

    suspend fun signUp(signUpRequest: SignUpRequest): DataResult<Any>

    suspend fun verifyAccount(verifyRequest: VerifyRequest): DataResult<Any>
}
