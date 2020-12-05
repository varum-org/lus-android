package com.vtnd.lus.di

import com.vtnd.lus.ui.auth.AuthViewModel
import com.vtnd.lus.ui.auth.login.LoginViewModel
import com.vtnd.lus.ui.auth.register.RegisterViewModel
import com.vtnd.lus.ui.auth.verify.VerifyViewModel
import com.vtnd.lus.ui.auth.welcome.WelcomeViewModel
import com.vtnd.lus.ui.intro.IntroSlideViewModel
import com.vtnd.lus.ui.main.MainViewModel
import com.vtnd.lus.ui.main.container.ContainerViewModel
import com.vtnd.lus.ui.main.container.favorite.FavoriteViewModel
import com.vtnd.lus.ui.main.container.home.HomeViewModel
import com.vtnd.lus.ui.main.container.idolDetail.IdolDetailViewModel
import com.vtnd.lus.ui.main.container.message.MessageViewModel
import com.vtnd.lus.ui.main.container.notification.NotificationViewModel
import com.vtnd.lus.ui.main.container.profile.ProfileViewModel
import com.vtnd.lus.ui.main.container.room.RoomViewModel
import com.vtnd.lus.ui.main.container.search.SearchViewModel
import com.vtnd.lus.ui.splash.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * You can create your ViewModel with scope, however it is not required because
 * 1 ViewModel can be used by several LifeCycleOwners.
 */
val viewModelModule = module {
    viewModel {
        SplashViewModel(
            repoRepository = get(),
            tokenRepository = get(),
            userRepository = get()
        )
    }
    viewModel { IntroSlideViewModel(repoRepository = get()) }
    viewModel { AuthViewModel() }
    viewModel {
        RoomViewModel(
            userRepository = get(),
            roomJsonAdapter = get(),
            roomResponseJsonAdapter = get()
        )
    }
    viewModel {
        MessageViewModel(
            userRepository = get(),
            messageJsonAdapter = get(),
            roomJsonAdapter = get()
        )
    }
    viewModel {
        MainViewModel(
            userRepository = get(),
            tokenRepository = get(),
            repoRepository = get()
        )
    }
    viewModel { ContainerViewModel() }
    viewModel {
        HomeViewModel(
            userRepository = get(),
            tokenRepository = get()
        )
    }
    viewModel { SearchViewModel(userRepository = get()) }
    viewModel { FavoriteViewModel() }
    viewModel { NotificationViewModel() }
    viewModel { ProfileViewModel(userRepository = get()) }
    viewModel {
        LoginViewModel(
            userRepository = get(),
            tokenRepository = get()
        )
    }
    viewModel { WelcomeViewModel() }
    viewModel {
        IdolDetailViewModel(
            userRepository = get(),
            tokenRepository = get(),
            roomJsonAdapter = get()
        )
    }
    viewModel { VerifyViewModel(userRepository = get()) }
    viewModel { RegisterViewModel(userRepository = get()) }
}
