package com.vtnd.lus.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.vtnd.lus.shared.liveData.observeLiveData
import com.sun.wsm.util.widget.*
import com.vtnd.lus.shared.extensions.handleDefaultApiError
import com.vtnd.lus.shared.widget.DialogManagerImpl

abstract class BaseActivity<viewBinding : ViewBinding, viewModel : BaseViewModel> :
    AppCompatActivity(), BaseView {

    protected abstract val viewModel: viewModel
    protected lateinit var viewBinding: viewBinding
    abstract fun inflateViewBinding(inflater: LayoutInflater): viewBinding

    protected abstract fun initialize()

    private lateinit var dialogManager: DialogManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = inflateViewBinding(layoutInflater)
        dialogManager = DialogManagerImpl(this)
        setContentView(viewBinding.root)
        initialize()
        registerLiveData()
    }

    override fun showLoading(isShow: Boolean) {
        if (isShow) showLoading() else hideLoading()
    }

    override fun showLoading() {
        dialogManager.showLoading()
    }

    override fun hideLoading() {
        dialogManager.hideLoading()
    }

    open fun registerLiveData() {
        viewModel.run {
            isLoading.observeLiveData(this@BaseActivity) {
                showLoading(it)
            }
            exception.observeLiveData(this@BaseActivity) {
                handleDefaultApiError(it)
            }
        }
    }
}
