package com.vtnd.lus.ui.main.container.historyIdol

import android.view.LayoutInflater
import com.vtnd.lus.R
import com.vtnd.lus.base.BaseFragment
import com.vtnd.lus.databinding.FragmentHistoryIdolBinding
import com.vtnd.lus.shared.extensions.initToolbarBase
import com.vtnd.lus.ui.main.container.historyIdol.adapter.HistoryIdolPagerAdapter
import com.vtnd.lus.ui.main.container.historyIdol.tab.TabHistoryIdolFragment
import kotlinx.android.synthetic.main.fragment_history_idol.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryIdolFragment : BaseFragment<FragmentHistoryIdolBinding, HistoryIdolViewModel>() {
    private lateinit var historyIdolPagerAdapter: HistoryIdolPagerAdapter

    override val viewModel: HistoryIdolViewModel by viewModel()

    override fun inflateViewBinding(inflater: LayoutInflater) =
        FragmentHistoryIdolBinding.inflate(inflater)

    override fun initialize() {
        initToolbarBase(
            getString(R.string.history_idol),
            isShowIconLeft = true
        )
        val titles =
            listOf(R.string.pending, R.string.approved, R.string.rejected, R.string.finished).map {
                getString(it)
            }
        historyIdolPagerAdapter = HistoryIdolPagerAdapter(titles, childFragmentManager)
        historyIdolViewPaper.apply {
            offscreenPageLimit = 1
            historyIdolTabLayout.setupWithViewPager(this)
            adapter = historyIdolPagerAdapter
        }
    }

    override fun registerLiveData() {
        super.registerLiveData()
    }

    companion object {
        fun newInstance() = HistoryIdolFragment()
    }
}
