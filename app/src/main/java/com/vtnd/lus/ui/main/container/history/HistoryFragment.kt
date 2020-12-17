package com.vtnd.lus.ui.main.container.history

import android.view.LayoutInflater
import com.vtnd.lus.R
import com.vtnd.lus.base.BaseFragment
import com.vtnd.lus.databinding.FragmentHistoryBinding
import com.vtnd.lus.shared.extensions.initToolbarBase
import com.vtnd.lus.ui.main.container.history.adapter.HistoryPagerAdapter
import kotlinx.android.synthetic.main.fragment_history.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryFragment : BaseFragment<FragmentHistoryBinding, HistoryViewModel>() {
    private lateinit var historyPagerAdapter: HistoryPagerAdapter

    override val viewModel: HistoryViewModel by viewModel()

    override fun inflateViewBinding(inflater: LayoutInflater) =
        FragmentHistoryBinding.inflate(inflater)

    override fun initialize() {
        initToolbarBase(
            getString(R.string.history),
            iconRight = R.drawable.ic_baseline_settings_24,
            isShowIconLeft = true
        )
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
