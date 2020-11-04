package com.vtnd.lus.ui.splash

import android.content.Intent
import android.view.LayoutInflater
import com.vtnd.lus.base.BaseActivity
import com.vtnd.lus.databinding.ActivitySplashBinding
import com.vtnd.lus.shared.extensions.delayTask
import com.vtnd.lus.ui.auth.AuthActivity
import com.vtnd.lus.ui.intro.IntroSlideActivity
import com.vtnd.lus.ui.main.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : BaseActivity<ActivitySplashBinding, SplashViewModel>() {
    override val viewModel: SplashViewModel by viewModel()

    override fun inflateViewBinding(inflater: LayoutInflater) =
        ActivitySplashBinding.inflate(inflater)

    override fun initialize() {
        viewModel.isOpenFirstApp?.let {
            delayTask({
                if (it) {
                    viewModel.setOpenFirstApp()
                    startActivity(Intent(applicationContext, IntroSlideActivity::class.java))
                } else startActivity(Intent(applicationContext, AuthActivity::class.java))
                finish()
            })
        }
    }
}
