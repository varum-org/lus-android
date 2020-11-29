package com.vtnd.lus.ui.main.container.home

import android.view.LayoutInflater
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.transition.MaterialContainerTransform
import com.vtnd.lus.R
import com.vtnd.lus.base.BaseFragment
import com.vtnd.lus.base.ItemViewHolder
import com.vtnd.lus.databinding.FragmentHomeBinding
import com.vtnd.lus.shared.AnimateType
import com.vtnd.lus.shared.decoration.HorizontalMarginItemDecoration
import com.vtnd.lus.shared.extensions.initToolbar
import com.vtnd.lus.shared.extensions.replaceFragment
import com.vtnd.lus.shared.liveData.observeLiveData
import com.vtnd.lus.ui.main.container.home.adapter.HotIdolAdapter
import com.vtnd.lus.ui.main.container.home.adapter.StoryCircleAdapter
import com.vtnd.lus.ui.main.container.idolDetail.IdolDetailFragment
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import kotlin.math.abs

class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {
    private val storyCircleAdapter = StoryCircleAdapter {}
    private val hotIdolAdapter = HotIdolAdapter { sharedElement, item ->
        val fragment = IdolDetailFragment.newInstance(item)
        fragment.sharedElementEnterTransition = MaterialContainerTransform()
        replaceFragment(
            containerId = R.id.container,
            fragment = fragment,
            animateType = AnimateType.SLIDE_TO_RIGHT,
            addToBackStack = true,
            sharedElement = sharedElement
        )
    }

    override val viewModel: HomeViewModel by viewModel()

    override fun inflateViewBinding(inflater: LayoutInflater) =
        FragmentHomeBinding.inflate(inflater)

    override fun initialize() {
        initToolbar(
            title = getString(R.string.app_name),
            iconRight = R.drawable.ic_chat_light
        )
        storyCircleRecyclerView.apply {
            setHasFixedSize(true)
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
    }

    override fun registerLiveData() = with(viewModel) {
        super.registerLiveData()
        storyIdols.observeLiveData(viewLifecycleOwner) {
            storyCircleAdapter.submitList(it)
        }
        hotIdols.observeLiveData(viewLifecycleOwner) {
            hotIdolAdapter.submitList(it?.map { idol ->
                ItemViewHolder(idol)
            })
        }
    }

    companion object {
        fun newInstance() = HomeFragment()
    }
}
