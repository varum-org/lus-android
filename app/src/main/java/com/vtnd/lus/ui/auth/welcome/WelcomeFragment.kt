package com.vtnd.lus.ui.auth.welcome

import android.content.Intent
import android.view.LayoutInflater
import com.vtnd.lus.R
import com.vtnd.lus.base.BaseFragment2
import com.vtnd.lus.databinding.FragmentWelcomeBinding
import com.vtnd.lus.shared.type.AuthType
import com.vtnd.lus.ui.auth.AuthActivity
import com.vtnd.lus.ui.main.MainActivity
import kotlinx.android.synthetic.main.fragment_welcome.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class WelcomeFragment : BaseFragment2<FragmentWelcomeBinding, WelcomeViewModel>() {

    override val viewModel: WelcomeViewModel by viewModel()

    override fun inflateViewBinding(inflater: LayoutInflater) =
        FragmentWelcomeBinding.inflate(inflater)

    override fun initialize() {
        loginButton.setOnClickListener {
            (activity as AuthActivity).switchFragment(AuthType.LOGIN)
        }
        registerButton.setOnClickListener {
            (activity as AuthActivity).switchFragment(AuthType.REGISTER)
        }
        skipText.setOnClickListener {
            startActivity(Intent(activity, MainActivity::class.java))
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
