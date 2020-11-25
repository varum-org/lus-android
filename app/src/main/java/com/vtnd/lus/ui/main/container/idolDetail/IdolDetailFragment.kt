package com.vtnd.lus.ui.main.container.idolDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.get
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.transition.MaterialContainerTransform
import com.vtnd.lus.R
import com.vtnd.lus.base.BaseFragment
import com.vtnd.lus.base.ItemViewHolder
import com.vtnd.lus.data.repository.source.remote.api.response.IdolResponse
import com.vtnd.lus.databinding.FragmentIdolDetailBinding
import com.vtnd.lus.shared.decoration.HorizontalMarginItemDecoration
import com.vtnd.lus.ui.main.container.idolDetail.adapter.GalleryAdapter
import com.vtnd.lus.ui.main.container.idolDetail.adapter.ServiceAdapter
import kotlinx.android.synthetic.main.fragment_idol_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.abs

class IdolDetailFragment : BaseFragment<FragmentIdolDetailBinding, IdolDetailViewModel>() {
    private val galleryAdapter = GalleryAdapter {}
    private val serviceAdapter = ServiceAdapter {}

    override val viewModel: IdolDetailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform()
    }

    override fun inflateViewBinding(inflater: LayoutInflater) =
        FragmentIdolDetailBinding.inflate(inflater)

    override fun initialize() {
        arguments?.apply {
            getParcelable<IdolResponse>(ARGUMENT_IDOL)?.let {
                idolImage.transitionName = it.idol.id
                Toast.makeText(activity, it.idol.nickName, Toast.LENGTH_SHORT).show()
            }
        }
        setupViewPager2()
        setupIndicators()
        setupCurrentIndicator(0)
        idolGalleryViewPager2.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                setupCurrentIndicator(position)
            }
        })
        setupServiceRecyclerView()
    }

    override fun registerLiveData() {
        super.registerLiveData()
    }

    private fun setupViewPager2() {
        idolGalleryViewPager2.apply {
            adapter = galleryAdapter.apply {
                submitList(listOf(
                    ItemViewHolder(Any()),
                    ItemViewHolder(Any()),
                    ItemViewHolder(Any())
                ))
            }
            addItemDecoration(HorizontalMarginItemDecoration(context, R.dimen.dp_16))
            offscreenPageLimit = 1
        }
    }

    private fun setupServiceRecyclerView() {
        idolServiceRecyclerView.apply {
            setHasFixedSize(true)
            adapter = serviceAdapter.apply {
                submitList(listOf(
                    ItemViewHolder(Any()),
                    ItemViewHolder(Any()),
                    ItemViewHolder(Any()),
                    ItemViewHolder(Any())
                ))
            }
        }
    }

    private fun setupIndicators() {
        val indicators = arrayOfNulls<ImageView>(galleryAdapter.itemCount)
        val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins(8, 0, 8, 0)
        for (i in indicators.indices) {
            indicators[i] = ImageView(requireContext())
            indicators[i].apply {
                this?.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.indicator_inactive
                    )
                )
                this?.layoutParams = layoutParams
            }
            indicatorsContainer.addView(indicators[i])
        }
    }

    private fun setupCurrentIndicator(index: Int) {
        val childCount = indicatorsContainer.childCount
        for (i in 0 until childCount) {
            val imageView = indicatorsContainer[i] as ImageView
            if (i == index) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.indicator_active
                    )
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.indicator_inactive
                    )
                )

            }
        }
    }

    companion object {
        private const val ARGUMENT_IDOL = "ARGUMENT_IDOL"

        fun newInstance(idol: IdolResponse) = IdolDetailFragment().apply {
            arguments = bundleOf(ARGUMENT_IDOL to idol)
        }
    }
}
