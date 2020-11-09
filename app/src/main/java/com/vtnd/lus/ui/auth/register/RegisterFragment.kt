package com.vtnd.lus.ui.auth.register

import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.vtnd.lus.R
import com.vtnd.lus.base.BaseFragment
import com.vtnd.lus.databinding.FragmentRegisterBinding
import com.vtnd.lus.shared.enum.AuthEnum
import com.vtnd.lus.shared.extensions.listenToViews
import com.vtnd.lus.shared.extensions.setupDismissKeyBoard
import com.vtnd.lus.ui.auth.AuthActivity
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
                Toast.makeText(
                    activity,
                    "username:${usernameInput.editText?.text}-password:${passwordInput.editText?.text}",
                    Toast.LENGTH_SHORT
                ).show()
            }
            R.id.signInText -> {
                (activity as AuthActivity).switchFragment(AuthEnum.LOGIN)
            }
        }
    }

    companion object {
        fun newInstance() = RegisterFragment()
    }
}