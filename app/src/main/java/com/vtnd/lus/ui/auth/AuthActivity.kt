package com.vtnd.lus.ui.auth

import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import com.vtnd.lus.R
import com.vtnd.lus.base.BaseActivity
import com.vtnd.lus.databinding.ActivityAuthBinding
import com.vtnd.lus.shared.type.AuthType
import com.vtnd.lus.shared.extensions.addFragmentToActivity
import com.vtnd.lus.shared.extensions.switchFragment
import com.vtnd.lus.ui.auth.login.LoginFragment
import com.vtnd.lus.ui.auth.register.RegisterFragment
import com.vtnd.lus.ui.auth.welcome.WelcomeFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthActivity : BaseActivity<ActivityAuthBinding, AuthViewModel>() {
    override val viewModel: AuthViewModel by viewModel()
    private val welcomeFragment = WelcomeFragment.newInstance()
    private val loginFragment = LoginFragment.newInstance()
    private val registerFragment = RegisterFragment.newInstance()
    private lateinit var currentFragment: Fragment

    override fun inflateViewBinding(inflater: LayoutInflater) =
        ActivityAuthBinding.inflate(inflater)

    override fun initialize() {
        viewModel.isOpenFirstApp?.let {
            currentFragment = welcomeFragment
            if (it) {
                viewModel.setOpenFirstApp()
                currentFragment = welcomeFragment
                addFragmentToActivity(R.id.auth, welcomeFragment, false)
            } else {
                currentFragment = loginFragment
                addFragmentToActivity(R.id.auth, loginFragment, false)
            }
        }
    }

    fun switchFragment(authType: AuthType) = when (authType) {
        AuthType.LOGIN -> {
            switchFragment(R.id.auth, currentFragment, loginFragment)
            currentFragment = loginFragment
        }
        AuthType.WELCOME -> {
            switchFragment(R.id.auth, currentFragment, welcomeFragment)
            currentFragment = welcomeFragment
        }
        AuthType.REGISTER -> {
            switchFragment(R.id.auth, currentFragment, registerFragment)
            currentFragment = registerFragment
        }
    }
}
