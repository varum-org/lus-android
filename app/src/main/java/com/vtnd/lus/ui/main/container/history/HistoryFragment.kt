package com.vtnd.lus.ui.main.container.history

import android.view.LayoutInflater
import com.vtnd.lus.base.BaseFragment
import com.vtnd.lus.databinding.FragmentHistoryBinding
import com.vtnd.lus.ui.main.container.history.adapter.HistoryPagerAdapter
import kotlinx.android.synthetic.main.fragment_history.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryFragment : BaseFragment<FragmentHistoryBinding, HistoryViewModel>() {
    private lateinit var historyPagerAdapter: HistoryPagerAdapter

    override val viewModel: HistoryViewModel by viewModel()

    override fun inflateViewBinding(inflater: LayoutInflater) =
        FragmentHistoryBinding.inflate(inflater)

    override fun initialize() {
        historyPagerAdapter = HistoryPagerAdapter(childFragmentManager)
        historyViewPaper.apply {
            offscreenPageLimit = 1
            historyTabLayout.setupWithViewPager(this)
            adapter = historyPagerAdapter
        }
    }

    override fun registerLiveData() {
        super.registerLiveData()
    }

    companion object {
        fun newInstance() = HistoryFragment()
    }
}
