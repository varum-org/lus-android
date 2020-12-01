package com.vtnd.lus.ui.auth

import android.view.LayoutInflater
import com.vtnd.lus.R
import com.vtnd.lus.base.BaseActivity
import com.vtnd.lus.databinding.ActivityAuthBinding
import com.vtnd.lus.shared.extensions.addFragmentToActivity
import com.vtnd.lus.shared.extensions.clearAllFragment
import com.vtnd.lus.shared.extensions.goBackFragment
import com.vtnd.lus.ui.auth.verify.VerifyFragment
import com.vtnd.lus.ui.auth.welcome.WelcomeFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthActivity : BaseActivity<ActivityAuthBinding, AuthViewModel>() {
    override val viewModel: AuthViewModel by viewModel()

    override fun inflateViewBinding(inflater: LayoutInflater) =
        ActivityAuthBinding.inflate(inflater)

    override fun initialize() {
        addFragmentToActivity(R.id.auth, WelcomeFragment.newInstance(), false)
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun onBackPressed() {
        val isShowPreviousPage = supportFragmentManager.backStackEntryCount > 0
        val verifyFragment =
            supportFragmentManager.findFragmentByTag(TAG_FRAGMENT_VERIFY) as VerifyFragment?
        if (isShowPreviousPage) {
            if (!(verifyFragment == null || !verifyFragment.isVisible))
                goBackFragment()
            else {
                clearAllFragment()
                goBackFragment()
            }
        } else{
            super.onBackPressed()
            overridePendingTransition(R.anim.nothing, R.anim.bottom_down)
        }
    }

    companion object {
        const val TAG_FRAGMENT_VERIFY = "TAG_FRAGMENT_VERIFY"
    }
}
