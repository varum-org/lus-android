package com.vtnd.lus.ui.main.container.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class ContainerViewPagerAdapter(
    manager: FragmentManager,
    private val fragments: List<Fragment>
) : FragmentStatePagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int) = fragments[position]

    override fun getCount() = fragments.size
}
