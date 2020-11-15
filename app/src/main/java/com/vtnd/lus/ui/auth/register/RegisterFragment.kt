package com.vtnd.lus.ui.auth.register

import android.view.LayoutInflater
import android.view.View
import com.vtnd.lus.R
import com.vtnd.lus.base.BaseFragment
import com.vtnd.lus.data.repository.source.remote.api.request.SignUpRequest
import com.vtnd.lus.databinding.FragmentRegisterBinding
import com.vtnd.lus.shared.AnimateType
import com.vtnd.lus.shared.extensions.listenToViews
import com.vtnd.lus.shared.extensions.replaceFragment
import com.vtnd.lus.shared.extensions.setupDismissKeyBoard
import com.vtnd.lus.shared.liveData.observeLiveData
import com.vtnd.lus.shared.type.AuthType
import com.vtnd.lus.shared.type.ValidateErrorType.*
import com.vtnd.lus.ui.auth.AuthActivity
import com.vtnd.lus.ui.auth.verify.VerifyFragment
import kotlinx.android.synthetic.main.fragment_register.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterFragment : BaseFragment<FragmentRegisterBinding, RegisterViewModel>(),
    View.OnClickListener {

    override val viewModel: RegisterViewModel by viewModel()

    override fun inflateViewBinding(inflater: LayoutInflater) =
        FragmentRegisterBinding.inflate(inflater)

    override fun initialize() {
        setupDismissKeyBoard(activity, registerLayout)
        listenToViews(registerButton, signInText)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.registerButton -> {
                viewModel.signUp(
                    SignUpRequest(
                        userNameInput.editText?.text.toString(),
                        emailInput.editText?.text.toString(),
                        passwordInput.editText?.text.toString(),
                        phoneInput.editText?.text.toString()
                    )
                )
            }
            R.id.signInText -> {
                (activity as AuthActivity).switchFragment(AuthType.LOGIN)
            }
        }
    }

    override fun registerLiveData() = with(viewModel) {
        super.registerLiveData()
        emailError.observeLiveData(viewLifecycleOwner, ::handleValidateEmail)
        passwordError.observeLiveData(viewLifecycleOwner, ::handleValidatePassword)
        userNameError.observeLiveData(viewLifecycleOwner, ::handleValidateUserName)
        phoneError.observeLiveData(viewLifecycleOwner, ::handleValidatePhone)
        signUpResponse.observeLiveData(viewLifecycleOwner) {
            handleSignUnSuccess()
        }
    }

    override fun onStop() {
        super.onStop()
        onHideSoftKeyBoard()
    }

    private fun handleValidateEmail(emailError: EmailErrorType) {
        emailInput.error = emailError.message
    }

    private fun handleValidateUserName(usernameError: UserNameErrorType) {
        userNameInput.error = usernameError.message
    }

    private fun handleValidatePassword(passwordError: PasswordErrorType) {
        passwordInput.error = passwordError.message
    }

    private fun handleValidatePhone(phoneError: PhoneErrorType) {
        phoneInput.error = phoneError.message
    }

    private fun handleSignUnSuccess() {
        replaceFragment(R.id.auth,
            VerifyFragment.newInstance(emailInput.editText?.text.toString()),
            true,
            animateType = AnimateType.SLIDE_TO_RIGHT)
    }

    companion object {
        fun newInstance() = RegisterFragment()
    }
}
