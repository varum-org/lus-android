package com.vtnd.lus.ui.auth.verify

import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.os.bundleOf
import com.vtnd.lus.R
import com.vtnd.lus.base.BaseFragment2
import com.vtnd.lus.data.repository.source.remote.api.request.VerifyRequest
import com.vtnd.lus.databinding.FragmentVerifyBinding
import com.vtnd.lus.shared.extensions.*
import com.vtnd.lus.shared.liveData.observeLiveData
import com.vtnd.lus.shared.type.AuthType
import com.vtnd.lus.shared.type.ValidateErrorType
import com.vtnd.lus.ui.auth.AuthActivity
import kotlinx.android.synthetic.main.fragment_verify.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class VerifyFragment : BaseFragment2<FragmentVerifyBinding, VerifyViewModel>() {
    private var email: String? = ""

    override val viewModel: VerifyViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            email = getString(ARGUMENT_EMAIL)
        }
    }

    override fun inflateViewBinding(inflater: LayoutInflater) =
        FragmentVerifyBinding.inflate(inflater)

    override fun initialize() {
        verifyButton.safeClick {
            viewModel.verifyAccount(VerifyRequest(
                email,
                codeOtpView.text.toString()
            ))
        }
        codeOtpView.setOtpCompletionListener {
            viewModel.verifyAccount(VerifyRequest(
                email,
                codeOtpView.text.toString()
            ))
        }
    }

    override fun showLoading() {
        verifyButton.invisible()
        progress.visible()

    }

    override fun hideLoading() {
        verifyButton.visible()
        progress.invisible()

    }

    override fun registerLiveData() = with(viewModel) {
        super.registerLiveData()
        emailError.observeLiveData(viewLifecycleOwner, ::handleValidateEmail)
        codeError.observeLiveData(viewLifecycleOwner, ::handleValidateEmailCode)
        verifyResponse.observeLiveData(viewLifecycleOwner) {
            handleVerifySuccess()
        }
    }

    override fun onStop() {
        super.onStop()
        onHideSoftKeyBoard()
    }

    private fun handleValidateEmail(emailError: ValidateErrorType.EmailErrorType) {
        emailError.message?.let { showError(it) }
    }

    private fun handleValidateEmailCode(emailCodeError: ValidateErrorType.CodeErrorType) {
        emailCodeError.message?.let { showError(it) }
    }

    private fun handleVerifySuccess() {
        showError(getString(R.string.verify_email_success))
        (activity as AuthActivity).switchFragment(AuthType.LOGIN)
        goBackFragment()
    }

    companion object {
        const val ARGUMENT_EMAIL = "ARGUMENT_EMAIL"

        fun newInstance(email: String) = VerifyFragment().apply {
            arguments = bundleOf(ARGUMENT_EMAIL to email)
        }
    }
}
