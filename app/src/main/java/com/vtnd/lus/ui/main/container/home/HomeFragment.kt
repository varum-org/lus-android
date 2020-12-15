package com.vtnd.lus.ui.main.container.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.transition.MaterialContainerTransform
import com.vtnd.lus.R
import com.vtnd.lus.base.BaseFragment
import com.vtnd.lus.databinding.FragmentHomeBinding
import com.vtnd.lus.shared.AnimateType
import com.vtnd.lus.shared.decoration.FlexibleGridSpacingItemDecoration
import com.vtnd.lus.shared.decoration.HorizontalMarginItemDecoration
import com.vtnd.lus.shared.decoration.VerticalSpaceItemDecoration
import com.vtnd.lus.shared.extensions.delayTask
import com.vtnd.lus.shared.extensions.initToolbar
import com.vtnd.lus.shared.extensions.replaceFragment
import com.vtnd.lus.shared.liveData.observeLiveData
import com.vtnd.lus.ui.auth.AuthActivity
import com.vtnd.lus.ui.main.container.home.adapter.HotIdolAdapter
import com.vtnd.lus.ui.main.container.home.adapter.RecommendAdapter
import com.vtnd.lus.ui.main.container.home.adapter.StoryCircleAdapter
import com.vtnd.lus.ui.main.container.idolDetail.IdolDetailFragment
import com.vtnd.lus.ui.main.container.room.RoomFragment
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.abs

class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(),
    SwipeRefreshLayout.OnRefreshListener {
    private val storyCircleAdapter = StoryCircleAdapter { item ->
        val fragment = IdolDetailFragment.newInstance(item, item.idol!!.id)
        fragment.sharedElementEnterTransition = MaterialContainerTransform()
        replaceFragment(
            containerId = R.id.container,
            fragment = fragment,
            animateType = AnimateType.SLIDE_TO_RIGHT,
            addToBackStack = true
        )
    }
    private val hotIdolAdapter = HotIdolAdapter { sharedElement, item ->
        val fragment = IdolDetailFragment.newInstance(item, item.idol!!.id)
        fragment.sharedElementEnterTransition = MaterialContainerTransform()
        replaceFragment(
            containerId = R.id.container,
            fragment = fragment,
            animateType = AnimateType.SLIDE_TO_RIGHT,
            addToBackStack = true,
            sharedElement = sharedElement
        )
    }
    private val recommendAdapter = RecommendAdapter { item ->
        val fragment = IdolDetailFragment.newInstance(item, item.idol!!.id)
        fragment.sharedElementEnterTransition = MaterialContainerTransform()
        replaceFragment(
            containerId = R.id.container,
            fragment = fragment,
            animateType = AnimateType.SLIDE_TO_RIGHT,
            addToBackStack = true
        )
    }

    override val viewModel: HomeViewModel by viewModel()

    override fun inflateViewBinding(inflater: LayoutInflater) =
        FragmentHomeBinding.inflate(inflater)

    @ExperimentalCoroutinesApi
    override fun initialize() {
        homeRefresh.setOnRefreshListener(this)
        initToolbar(
            title = getString(R.string.app_name),
            iconRight = R.drawable.ic_chat_light
        ) {
            viewModel.checkLogin() {
                if (it) replaceFragment(
                    containerId = R.id.container,
                    fragment = RoomFragment.newInstance(),
                    addToBackStack = true,
                    animateType = AnimateType.SLIDE_TO_RIGHT
                )
                else {
                    activity?.apply {
                        showLoading(true)
                        delayTask({
                            showLoading(false)
                            startActivity(Intent(this, AuthActivity::class.java))
                            overridePendingTransition(R.anim.bottom_up, R.anim.nothing)
                        }, 800)
                    }
                }
            }
        }
        storyCircleRecyclerView.apply {
            adapter = storyCircleAdapter
        }
        hotIdolRecyclerView.apply {
            adapter = hotIdolAdapter
            offscreenPageLimit = 1
            val nextItemVisiblePx = resources.getDimension(R.dimen.dp_80)
            val currentItemHorizontalMarginPx = resources.getDimension(R.dimen.dp_80)
            val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx
            val pageTransformer = ViewPager2.PageTransformer { page: View, position: Float ->
                page.translationX = -pageTranslationX * position
                page.scaleY = 1 - (0.25f * abs(position))
            }
            setPageTransformer(pageTransformer)
            addItemDecoration(HorizontalMarginItemDecoration(context, R.dimen.dp_80))
        }
        recommendRecyclerView.apply {
            adapter = recommendAdapter
            addItemDecoration(
                FlexibleGridSpacingItemDecoration(
                    top = resources.getDimensionPixelOffset(R.dimen.dp_8),
                    bottom = resources.getDimensionPixelOffset(R.dimen.dp_8),
                    left = resources.getDimensionPixelOffset(R.dimen.dp_16),
                    right = resources.getDimensionPixelOffset(R.dimen.dp_16),
                    middle = resources.getDimensionPixelOffset(R.dimen.dp_8)
                )
            )
        }
    }

    override fun registerLiveData() = with(viewModel) {
        super.registerLiveData()
        storyIdols.observeLiveData(viewLifecycleOwner) {
            storyCircleAdapter.submitList(it)
        }
        hotIdols.observeLiveData(viewLifecycleOwner) {
            homeRefresh.isRefreshing = false
            hotIdolAdapter.submitList(it)
        }
        recommendIdols.observeLiveData(viewLifecycleOwner) {
            recommendAdapter.submitList(it)
        }
    }

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onRefresh() {
        viewModel.initialHotIdols()
    }
}
