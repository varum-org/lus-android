package com.vtnd.lus.ui.main.container.history.tab

import android.view.LayoutInflater
import com.vtnd.lus.base.BaseFragment
import com.vtnd.lus.databinding.FragmentHistoryBinding
import com.vtnd.lus.databinding.FragmentTabHistoryBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class TabHistoryFragment : BaseFragment<FragmentTabHistoryBinding, TabHistoryViewModel>() {

    override val viewModel: TabHistoryViewModel by viewModel()

    override fun inflateViewBinding(inflater: LayoutInflater) =
        FragmentTabHistoryBinding.inflate(inflater)

    override fun initialize() {
    }

    override fun registerLiveData() {
        super.registerLiveData()
    }

    companion object {
        fun newInstance() = TabHistoryFragment()
    }
}
