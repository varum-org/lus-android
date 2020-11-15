package com.vtnd.lus.ui.auth.login

import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.vtnd.lus.R
import com.vtnd.lus.base.BaseFragment
import com.vtnd.lus.databinding.FragmentLoginBinding
import com.vtnd.lus.shared.type.AuthType
import com.vtnd.lus.shared.type.ValidateErrorType.EmailErrorType
import com.vtnd.lus.shared.type.ValidateErrorType.PasswordErrorType
import com.vtnd.lus.shared.extensions.listenToViews
import com.vtnd.lus.shared.extensions.setupDismissKeyBoard
import com.vtnd.lus.shared.liveData.observeLiveData
import com.vtnd.lus.ui.auth.AuthActivity
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>(),
    View.OnClickListener {

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
                viewModel.signIn(emailInput.editText?.text.toString(),
                    passwordInput.editText?.text.toString())
            }
            R.id.signUpText -> {
                (activity as AuthActivity).switchFragment(AuthType.REGISTER)
            }
        }
    }

    override fun registerLiveData() = with(viewModel) {
        super.registerLiveData()
        signInResponse.observeLiveData(viewLifecycleOwner) {
//            Toast.makeText(activity, it?.user?.userName, Toast.LENGTH_SHORT).show()
        }
        emailError.observeLiveData(viewLifecycleOwner, ::handleValidateEmail)
        passwordError.observeLiveData(viewLifecycleOwner, ::handleValidatePassword)
    }

    override fun onStop() {
        super.onStop()
        onHideSoftKeyBoard()
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
