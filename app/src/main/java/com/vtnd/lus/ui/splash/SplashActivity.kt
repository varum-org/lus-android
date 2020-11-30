package com.vtnd.lus.ui.splash

import android.content.Intent
import android.view.LayoutInflater
import com.vtnd.lus.R
import com.vtnd.lus.base.BaseActivity2
import com.vtnd.lus.databinding.ActivitySplashBinding
import com.vtnd.lus.shared.liveData.observeLiveData
import com.vtnd.lus.ui.auth.AuthActivity
import com.vtnd.lus.ui.intro.IntroSlideActivity
import com.vtnd.lus.ui.main.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : BaseActivity2<ActivitySplashBinding, SplashViewModel>() {
    override val viewModel: SplashViewModel by viewModel()

    override fun inflateViewBinding(inflater: LayoutInflater) =
            ActivitySplashBinding.inflate(inflater)

    override fun initialize() {}

    override fun showLoading() {}

    override fun hideLoading() {}

    override fun registerLiveData() = with(viewModel) {
        super.registerLiveData()
        isSaved.observeLiveData(this@SplashActivity) {
            startActivity(Intent(applicationContext, MainActivity::class.java))
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            finish()
        }
        isSavedService.observeLiveData(this@SplashActivity) {
            if (isOpenFirstApp) {
                startActivity(Intent(applicationContext, IntroSlideActivity::class.java))
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                finish()
            } else {
                if (!token) {
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                    finish()
                } else viewModel.saveUserToLocal()
            }
        }
    }
}
