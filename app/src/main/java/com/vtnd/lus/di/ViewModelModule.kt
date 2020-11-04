package com.vtnd.lus.di

import com.vtnd.lus.ui.auth.AuthViewModel
import com.vtnd.lus.ui.auth.login.LoginViewModel
import com.vtnd.lus.ui.auth.welcome.WelcomeViewModel
import com.vtnd.lus.ui.intro.IntroSlideViewModel
import com.vtnd.lus.ui.main.MainViewModel
import com.vtnd.lus.ui.main.container.ContainerViewModel
import com.vtnd.lus.ui.main.container.favorite.FavoriteViewModel
import com.vtnd.lus.ui.main.container.home.HomeViewModel
import com.vtnd.lus.ui.main.container.notification.NotificationViewModel
import com.vtnd.lus.ui.main.container.profilte.ProfileViewModel
import com.vtnd.lus.ui.main.container.search.SearchViewModel
import com.vtnd.lus.ui.splash.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * You can create your ViewModel with scope, however it is not required because
 * 1 ViewModel can be used by several LifeCycleOwners.
 */
val viewModelModule = module {
    viewModel { SplashViewModel(repoRepository = get()) }
    viewModel { IntroSlideViewModel(repoRepository = get()) }
    viewModel { AuthViewModel(repoRepository = get()) }
    viewModel { MainViewModel(userRepository = get()) }
    viewModel { ContainerViewModel() }
    viewModel { HomeViewModel() }
    viewModel { SearchViewModel() }
    viewModel { FavoriteViewModel() }
    viewModel { NotificationViewModel() }
    viewModel { ProfileViewModel() }
    viewModel { LoginViewModel() }
    viewModel { WelcomeViewModel() }
}
