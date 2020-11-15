package com.vtnd.lus.ui.splash

import com.vtnd.lus.base.BaseViewModel
import com.vtnd.lus.data.RepoRepository
import com.vtnd.lus.data.TokenRepository

class SplashViewModel(
    repoRepository: RepoRepository,
    tokenRepository: TokenRepository
) : BaseViewModel() {
    val isOpenFirstApp = repoRepository.isOpenFirstApp()
    val token = !tokenRepository.getToken().isNullOrBlank()
}
