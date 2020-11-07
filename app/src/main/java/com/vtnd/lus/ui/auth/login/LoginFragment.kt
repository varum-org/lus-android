package com.vtnd.lus.ui.auth.login

import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.vtnd.lus.R
import com.vtnd.lus.base.BaseFragment
import com.vtnd.lus.databinding.FragmentLoginBinding
import com.vtnd.lus.shared.extensions.listenToViews
import com.vtnd.lus.shared.extensions.setupDismissKeyBoard
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>(), View.OnClickListener {

    override val viewModel: LoginViewModel by viewModel()

    override fun inflateViewBinding(inflater: LayoutInflater) =
        FragmentLoginBinding.inflate(inflater)

    override fun initialize() {
        setupDismissKeyBoard(activity, loginLayout)
        listenToViews(loginButton, signUpText)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.loginButton -> {
                Toast.makeText(
                    activity,
                    "username:${usernameInput.editText?.text}-password:${passwordInput.editText?.text}",
                    Toast.LENGTH_SHORT
                ).show()
            }
            R.id.signUpText -> {
            }
        }
    }

    companion object {
        fun newInstance() = LoginFragment()
    }
}
