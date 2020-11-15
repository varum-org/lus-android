package com.vtnd.lus.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.vtnd.lus.shared.constants.Constants
import com.vtnd.lus.shared.extensions.getCalendar
import com.vtnd.lus.shared.extensions.handleDefaultApiError
import com.vtnd.lus.shared.extensions.hideChildFragment
import com.vtnd.lus.shared.extensions.showChildFragment
import com.vtnd.lus.shared.liveData.observeLiveData
import com.vtnd.lus.shared.widget.DialogManagerImpl
import com.vtnd.lus.ui.main.empty.EmptyFragment

abstract class BaseFragment<viewBinding : ViewBinding, viewModel : BaseViewModel> :
    Fragment(), BaseView {

    protected abstract val viewModel: viewModel
    private var _viewBinding: viewBinding? = null
    protected val viewBinding get() = _viewBinding!! // ktlint-disable
    private val noDataFragment by lazy { EmptyFragment.newInstance() }
    val currentCalendar by lazy { getCalendar() }
    val defaultCalendar by lazy { getCalendar(Constants.TIME_ZONE_UTC) }

    abstract fun inflateViewBinding(inflater: LayoutInflater): viewBinding

    protected abstract fun initialize()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DialogManagerImpl(getContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = inflateViewBinding(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
        registerLiveData()
    }

    override fun showLoading(isShow: Boolean) {
        if (isShow) showLoading() else hideLoading()
    }

    override fun showLoading() {
        (activity as? BaseActivity<*, *>)?.showLoading()
    }

    override fun hideLoading() {
        (activity as? BaseActivity<*, *>)?.hideLoading()
    }

    fun showChildNoDataFragment(@IdRes containerId: Int) {
        showChildFragment(containerId, showFragment = noDataFragment)
    }

    fun hideChildNoDataFragment() {
        hideChildFragment(noDataFragment)
    }

    /**
     * Fragments outlive their views. Make sure you clean up any references to
     * the binding class instance in the fragment's onDestroyView() method.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    open fun registerLiveData() = with(viewModel) {
        isLoading.observeLiveData(viewLifecycleOwner) {
            showLoading(it)
        }
        exception.observeLiveData(viewLifecycleOwner) {
            (activity as? BaseActivity<*, *>)?.handleDefaultApiError(it)
        }
    }

    fun onHideSoftKeyBoard() {
        val inputMng: InputMethodManager =
            context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMng.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
    }
}
