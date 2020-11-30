package com.vtnd.lus.ui.auth.login

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import com.vtnd.lus.R
import com.vtnd.lus.base.BaseFragment2
import com.vtnd.lus.databinding.FragmentLoginBinding
import com.vtnd.lus.shared.extensions.*
import com.vtnd.lus.shared.liveData.observeLiveData
import com.vtnd.lus.shared.type.AuthType
import com.vtnd.lus.shared.type.ValidateErrorType.EmailErrorType
import com.vtnd.lus.shared.type.ValidateErrorType.PasswordErrorType
import com.vtnd.lus.ui.auth.AuthActivity
import com.vtnd.lus.ui.auth.register.RegisterFragment
import com.vtnd.lus.ui.main.MainActivity
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment2<FragmentLoginBinding, LoginViewModel>(),
    View.OnClickListener {

    override val viewModel: LoginViewModel by viewModel()

    override fun inflateViewBinding(inflater: LayoutInflater) =
        FragmentLoginBinding.inflate(inflater)

    override fun initialize() {
        setupDismissKeyBoard(activity, loginLayout)
        listenToViews(loginButton, signUpText, skipText)
    }


    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.loginButton -> {
                viewModel.signIn(emailInput.editText?.text.toString(),
                    passwordInput.editText?.text.toString())
            }
            R.id.signUpText -> {
                replaceFragment(R.id.auth, RegisterFragment.newInstance(), true)
            }
            R.id.skipText ->   {
                (activity as AuthActivity).clearAllFragment()
                goBackFragment()
            }
        }
    }

    override fun registerLiveData() = with(viewModel) {
        super.registerLiveData()
        signInResponse.observeLiveData(viewLifecycleOwner) {
            activity?.apply {
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                finish()
            }
        }
        emailError.observeLiveData(viewLifecycleOwner, ::handleValidateEmail)
        passwordError.observeLiveData(viewLifecycleOwner, ::handleValidatePassword)
    }

    override fun onStop() {
        super.onStop()
        onHideSoftKeyBoard()
    }

    override fun showLoading() {
        loginButton.invisible()
        progress.visible()
    }

    override fun hideLoading() {
        loginButton.visible()
        progress.invisible()
    }

    private fun handleValidateEmail(emailError: EmailErrorType) {
        emailInput.error = emailError.message
    }


    private fun handleValidatePassword(passwordError: PasswordErrorType) {
        passwordInput.error = passwordError.message
    }


    companion object {
        fun newInstance() = LoginFragment()
    }
}
