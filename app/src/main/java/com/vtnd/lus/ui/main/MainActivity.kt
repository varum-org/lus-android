package com.vtnd.lus.ui.main

import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.WindowManager
import com.vtnd.lus.R
import com.vtnd.lus.base.BaseActivity
import com.vtnd.lus.databinding.ActivityMainBinding
import com.vtnd.lus.shared.extensions.addFragmentToActivity
import com.vtnd.lus.shared.liveData.observeLiveData
import com.vtnd.lus.ui.auth.AuthActivity
import com.vtnd.lus.ui.main.container.ContainerFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    override val viewModel: MainViewModel by viewModel()

    override fun inflateViewBinding(inflater: LayoutInflater) =
        ActivityMainBinding.inflate(inflater)

    override fun initialize() {
        addFragmentToActivity(R.id.container, ContainerFragment.newInstance(), false)
    }

    @ExperimentalCoroutinesApi
    override fun registerLiveData() = with(viewModel) {
        super.registerLiveData()
        isLogin.observeLiveData(this@MainActivity) {
            if (it) {
                logoutEvent.observeLiveData(this@MainActivity) {
                    startActivity(Intent(applicationContext, AuthActivity::class.java))
                    finish()
                }
            }
        }
    }
}
