package com.vtnd.lus.ui.main.container.historyIdol.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.vtnd.lus.ui.main.container.historyIdol.tab.TabHistoryIdolFragment
import com.vtnd.lus.ui.main.container.historyIdol.tab.TabHistoryIdolFragment.Companion.ARG_STATUS_CODE_IDOL

class HistoryIdolPagerAdapter(private val titles: List<String>, fm: FragmentManager) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount(): Int = 4

    override fun getItem(i: Int): Fragment {
        val fragment = TabHistoryIdolFragment.newInstance()
        fragment.arguments = Bundle().apply {
            putInt(ARG_STATUS_CODE_IDOL, i + 1)
        }
        return fragment
    }

    override fun getPageTitle(position: Int) = titles[position]
}