package com.vtnd.lus.ui.splash

import com.vtnd.lus.base.BaseViewModel
import com.vtnd.lus.data.RepoRepository

class SplashViewModel(private val repoRepository: RepoRepository) : BaseViewModel() {
    val isOpenFirstApp = repoRepository.isOpenFirstApp()

    fun setOpenFirstApp() {
        repoRepository.setOpenFirstApp(false)
    }
}
