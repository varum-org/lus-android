package com.vtnd.lus.data.repository.source

import com.vtnd.lus.data.repository.source.remote.api.response.BaseResponse
import com.vtnd.lus.data.repository.source.remote.api.response.SignInResponse

interface UserDataSource {

    interface Local

    interface Remote {
        suspend fun signIn(email: String, password: String): BaseResponse<SignInResponse>
    }
}
