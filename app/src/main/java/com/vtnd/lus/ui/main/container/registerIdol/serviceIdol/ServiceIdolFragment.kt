package com.vtnd.lus.ui.main.container.registerIdol.serviceIdol

import android.view.LayoutInflater
import com.vtnd.lus.R
import com.vtnd.lus.base.BaseFragment
import com.vtnd.lus.databinding.*
import com.vtnd.lus.shared.extensions.initToolbar
import com.vtnd.lus.ui.main.container.registerIdol.RegisterIdolViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ServiceIdolFragment : BaseFragment<FragmentServiceIdolBinding,
        RegisterIdolViewModel>() {

    override val viewModel: RegisterIdolViewModel by viewModel()

    override fun inflateViewBinding(inflater: LayoutInflater) =
        FragmentServiceIdolBinding.inflate(inflater)

    override fun initialize() {

    }

    override fun registerLiveData() {
        super.registerLiveData()
    }

    companion object {
        fun newInstance() = ServiceIdolFragment()
    }
}
