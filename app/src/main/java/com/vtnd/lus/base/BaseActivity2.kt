package com.vtnd.lus.base

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.vtnd.lus.shared.extensions.handleDefaultApiError
import com.vtnd.lus.shared.liveData.observeLiveData
import com.vtnd.lus.ui.main.MainActivity

abstract class BaseActivity2<viewBinding : ViewBinding, viewModel : BaseViewModel> :
    AppCompatActivity(), BaseView {

    protected abstract val viewModel: viewModel
    protected lateinit var viewBinding: viewBinding
    abstract fun inflateViewBinding(inflater: LayoutInflater): viewBinding

    protected abstract fun initialize()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = inflateViewBinding(layoutInflater)
        setContentView(viewBinding.root)
        initialize()
        registerLiveData()
    }

    override fun showLoading(isShow: Boolean) {
        if (isShow) showLoading() else hideLoading()
    }

    open fun registerLiveData() {
        viewModel.run {
            isLoading.observeLiveData(this@BaseActivity2) {
                showLoading(it)
            }
            exception.observeLiveData(this@BaseActivity2) {
                handleDefaultApiError(it) {
                    startActivity(Intent(this@BaseActivity2, MainActivity::class.java))
                    finish()
                }
            }
        }
    }
}
