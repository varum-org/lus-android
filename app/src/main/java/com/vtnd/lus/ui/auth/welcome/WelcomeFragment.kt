package com.vtnd.lus.ui.auth.welcome

import android.view.LayoutInflater
import com.vtnd.lus.base.BaseFragment
import com.vtnd.lus.databinding.FragmentLoginBinding
import com.vtnd.lus.databinding.FragmentWelcomeBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class WelcomeFragment : BaseFragment<FragmentWelcomeBinding, WelcomeViewModel>() {

    override val viewModel: WelcomeViewModel by viewModel()

    override fun inflateViewBinding(inflater: LayoutInflater) =
        FragmentWelcomeBinding.inflate(inflater)

    override fun initialize() {}

    companion object {
        fun newInstance() = WelcomeFragment()
    }
}
