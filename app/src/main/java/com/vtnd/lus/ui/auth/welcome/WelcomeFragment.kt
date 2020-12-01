package com.vtnd.lus.ui.auth.welcome

import android.view.LayoutInflater
import com.vtnd.lus.R
import com.vtnd.lus.base.BaseFragment2
import com.vtnd.lus.databinding.FragmentWelcomeBinding
import com.vtnd.lus.shared.extensions.replaceFragment
import com.vtnd.lus.ui.auth.login.LoginFragment
import com.vtnd.lus.ui.auth.register.RegisterFragment
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
                finish()
                overridePendingTransition(R.anim.nothing, R.anim.bottom_down)
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
