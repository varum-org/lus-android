package com.vtnd.lus.data

import com.vtnd.lus.data.repository.source.remote.api.response.SignInResponse
import com.vtnd.lus.shared.scheduler.DataResult

interface UserRepository {

    //Remote
    suspend fun signIn(email: String, password: String): DataResult<SignInResponse>
}
