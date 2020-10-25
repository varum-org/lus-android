package com.vtnd.lus.data.repository.source.remote

import com.vtnd.lus.data.repository.source.UserDataSource
import com.vtnd.lus.data.repository.source.remote.api.ApiService

class UserRemoteImpl(private val apiService: ApiService) : UserDataSource.Remote
