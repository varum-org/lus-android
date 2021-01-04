package com.vtnd.lus.ui.main.container

import android.view.LayoutInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vtnd.lus.R
import com.vtnd.lus.base.BaseFragment
import com.vtnd.lus.databinding.FragmentContainerBinding
import com.vtnd.lus.ui.main.container.adapter.ContainerViewPagerAdapter
import com.vtnd.lus.ui.main.container.favorite.FavoriteFragment
import com.vtnd.lus.ui.main.container.home.HomeFragment
import com.vtnd.lus.ui.main.container.notification.NotificationFragment
import com.vtnd.lus.ui.main.container.profile.ProfileFragment
import com.vtnd.lus.ui.main.container.search.SearchFragment
import kotlinx.android.synthetic.main.fragment_container.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ContainerFragment : BaseFragment<FragmentContainerBinding, ContainerViewModel>(),
    BottomNavigationView.OnNavigationItemSelectedListener,
    ViewPager.OnPageChangeListener {

    override val viewModel: ContainerViewModel by viewModel()
    private val home by lazy { HomeFragment.newInstance() }
    private val search by lazy { SearchFragment.newInstance() }
    private val favorite by lazy { FavoriteFragment.newInstance() }
    private val notification by lazy { NotificationFragment.newInstance() }
    private val profile by lazy { ProfileFragment.newInstance() }

    override fun inflateViewBinding(inflater: LayoutInflater) =
        FragmentContainerBinding.inflate(inflater)

    override fun initialize() {
        containerBottomNavigation.setOnNavigationItemSelectedListener(this)
        containerViewPaper.apply {
            addOnPageChangeListener(this@ContainerFragment)
            setSwipePagingEnabled(false)
            val fragments = listOf<Fragment>(home, search, favorite, notification, profile)
            adapter = ContainerViewPagerAdapter(childFragmentManager, fragments)
            currentItem = POSITION_NAVIGATE_HOME
        }
        containerBottomNavigation.menu.findItem(R.id.home).isChecked = true
        containerBottomNavigation.getOrCreateBadge(R.id.notification).apply {
            backgroundColor = resources.getColor(R.color.deep_purple_a200, context?.theme)
            number = 99
        }
    }

    override fun registerLiveData() = with(viewModel) {
        super.registerLiveData()
    }

    override fun onNavigationItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.home -> {
                containerViewPaper.currentItem = POSITION_NAVIGATE_HOME
                true
            }
            R.id.search -> {
                containerViewPaper.currentItem = POSITION_NAVIGATE_SEARCH
                true
            }
            R.id.favorite -> {
                containerViewPaper.currentItem = POSITION_NAVIGATE_FAVORITE
                true
            }
            R.id.notification -> {
                containerViewPaper.currentItem = POSITION_NAVIGATE_NOTIFICATION
                true
            }
            R.id.profile -> {
                containerViewPaper.currentItem = POSITION_NAVIGATE_PROFILE
                true
            }
            else -> false
        }

    override fun onPageScrollStateChanged(state: Int) {}

    override fun onPageScrolled(
        position: Int, positionOffset: Float,
        positionOffsetPixels: Int
    ) {
    }

    override fun onPageSelected(position: Int) {
        when (position) {
            POSITION_NAVIGATE_HOME ->
                containerBottomNavigation.menu.findItem(R.id.home).isChecked = true
            POSITION_NAVIGATE_SEARCH ->
                containerBottomNavigation.menu.findItem(R.id.search).isChecked = true
            POSITION_NAVIGATE_FAVORITE ->
                containerBottomNavigation.menu.findItem(R.id.favorite).isChecked = true
            POSITION_NAVIGATE_NOTIFICATION ->
                containerBottomNavigation.menu.findItem(R.id.notification).isChecked = true
            else -> containerBottomNavigation.menu.findItem(R.id.profile).isChecked = true
        }
    }

    companion object {
        const val POSITION_NAVIGATE_HOME = 0
        const val POSITION_NAVIGATE_SEARCH = 1
        const val POSITION_NAVIGATE_FAVORITE = 2
        const val POSITION_NAVIGATE_NOTIFICATION = 3
        const val POSITION_NAVIGATE_PROFILE = 4
        const val OFF_SCREEN_PAGE_LIMIT = 5

        fun newInstance() = ContainerFragment()
    }
}
