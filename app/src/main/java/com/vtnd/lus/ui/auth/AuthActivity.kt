package com.vtnd.lus.ui.auth

import android.view.LayoutInflater
import com.vtnd.lus.R
import com.vtnd.lus.base.BaseActivity
import com.vtnd.lus.databinding.ActivityAuthBinding
import com.vtnd.lus.shared.extensions.addFragmentToActivity
import com.vtnd.lus.ui.auth.login.LoginFragment
import com.vtnd.lus.ui.auth.welcome.WelcomeFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthActivity : BaseActivity<ActivityAuthBinding, AuthViewModel>() {
    override val viewModel: AuthViewModel by viewModel()

    override fun inflateViewBinding(inflater: LayoutInflater) =
        ActivityAuthBinding.inflate(inflater)

    override fun initialize() {
        viewModel.isOpenFirstApp?.let {
            if (it) {
                viewModel.setOpenFirstApp()
                addFragmentToActivity(R.id.auth, WelcomeFragment.newInstance(), false)
            } else
                addFragmentToActivity(R.id.auth, LoginFragment.newInstance(), false)
        }
    }
}
