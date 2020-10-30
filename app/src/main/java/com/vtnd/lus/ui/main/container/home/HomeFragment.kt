package com.vtnd.lus.ui.main.container.home

import android.view.LayoutInflater
import com.sun.wsm.util.extension.initToolbar
import com.vtnd.lus.R
import com.vtnd.lus.base.BaseFragment
import com.vtnd.lus.databinding.FragmentHomeBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<FragmentHomeBinding,
        HomeViewModel>() {

    override val viewModel: HomeViewModel by viewModel()

    override fun inflateViewBinding(inflater: LayoutInflater) =
        FragmentHomeBinding.inflate(inflater)

    override fun initialize() {
        initToolbar(
            title = getString(R.string.app_name),
            iconRight = R.drawable.ic_chat_light
        )
    }

    override fun registerLiveData() {
        super.registerLiveData()
    }

    companion object {
        fun newInstance() = HomeFragment()
    }
}
