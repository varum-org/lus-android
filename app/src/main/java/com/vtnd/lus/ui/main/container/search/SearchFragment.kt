package com.vtnd.lus.ui.main.container.search

import android.view.LayoutInflater
import com.vtnd.lus.base.BaseFragment
import com.vtnd.lus.databinding.FragmentNotificationBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : BaseFragment<FragmentNotificationBinding,
        SearchViewModel>() {

    override val viewModel: SearchViewModel by viewModel()

    override fun inflateViewBinding(inflater: LayoutInflater) =
        FragmentNotificationBinding.inflate(inflater)

    override fun initialize() {}

    override fun registerLiveData() {
        super.registerLiveData()
    }

    companion object {
        fun newInstance() = SearchFragment()
    }
}
