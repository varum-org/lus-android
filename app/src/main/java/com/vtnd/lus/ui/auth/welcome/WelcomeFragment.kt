package com.vtnd.lus.ui.auth.welcome

import android.content.Intent
import android.view.LayoutInflater
import com.vtnd.lus.R
import com.vtnd.lus.base.BaseFragment2
import com.vtnd.lus.databinding.FragmentWelcomeBinding
import com.vtnd.lus.shared.extensions.replaceFragment
import com.vtnd.lus.shared.type.AuthType
import com.vtnd.lus.ui.auth.AuthActivity
import com.vtnd.lus.ui.auth.login.LoginFragment
import com.vtnd.lus.ui.auth.register.RegisterFragment
import com.vtnd.lus.ui.main.MainActivity
import kotlinx.android.synthetic.main.fragment_welcome.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class WelcomeFragment : BaseFragment2<FragmentWelcomeBinding, WelcomeViewModel>() {

    override val viewModel: WelcomeViewModel by viewModel()

    override fun inflateViewBinding(inflater: LayoutInflater) =
        FragmentWelcomeBinding.inflate(inflater)

    override fun initialize() {
        loginButton.setOnClickListener {
            replaceFragment(R.id.auth, LoginFragment.newInstance(), true)
        }
        registerButton.setOnClickListener {
            replaceFragment(R.id.auth, RegisterFragment.newInstance(), true)
        }
        skipText.setOnClickListener {
            activity?.apply {
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                finish()
            }
        }
    }

    override fun showLoading() {

    }

    override fun hideLoading() {
    }

    companion object {
        fun newInstance() = WelcomeFragment()
    }
}
