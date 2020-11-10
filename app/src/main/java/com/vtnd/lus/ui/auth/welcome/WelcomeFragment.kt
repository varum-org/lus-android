package com.vtnd.lus.ui.auth.welcome

import android.view.LayoutInflater
import com.vtnd.lus.base.BaseFragment
import com.vtnd.lus.databinding.FragmentWelcomeBinding
import com.vtnd.lus.shared.type.AuthType
import com.vtnd.lus.ui.auth.AuthActivity
import kotlinx.android.synthetic.main.fragment_welcome.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class WelcomeFragment : BaseFragment<FragmentWelcomeBinding, WelcomeViewModel>() {

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
    }

    companion object {
        fun newInstance() = WelcomeFragment()
    }
}
