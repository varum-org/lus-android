package com.vtnd.lus.ui.main.container.history.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.vtnd.lus.ui.main.container.history.tab.TabHistoryFragment
import com.vtnd.lus.ui.main.container.history.tab.TabHistoryFragment.Companion.ARG_STATUS_CODE

class HistoryPagerAdapter(private val titles: List<String>, fm: FragmentManager) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount(): Int = 4

    override fun getItem(i: Int): Fragment {
        val fragment = TabHistoryFragment.newInstance()
        fragment.arguments = Bundle().apply {
            putInt(ARG_STATUS_CODE, i + 1)
        }
        return fragment
    }

    override fun getPageTitle(position: Int) = titles[position]
}