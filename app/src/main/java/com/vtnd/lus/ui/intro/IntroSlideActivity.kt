package com.vtnd.lus.ui.intro

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.viewpager2.widget.ViewPager2
import com.vtnd.lus.R
import com.vtnd.lus.base.BaseActivity
import com.vtnd.lus.base.ItemViewHolder
import com.vtnd.lus.databinding.ActivityIntroSlideBinding
import com.vtnd.lus.shared.extensions.safeClick
import com.vtnd.lus.ui.intro.adapter.IntroSlide
import com.vtnd.lus.ui.intro.adapter.IntroSlideAdapter
import com.vtnd.lus.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_intro_slide.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class IntroSlideActivity : BaseActivity<ActivityIntroSlideBinding, IntroSlideViewModel>() {
    private val introSlideAdapter by lazy { IntroSlideAdapter() }

    override val viewModel: IntroSlideViewModel by viewModel()

    override fun inflateViewBinding(inflater: LayoutInflater) =
        ActivityIntroSlideBinding.inflate(inflater)

    override fun initialize() {
        setContentView(R.layout.activity_intro_slide)
        setupIntroSlide()
        setupIndicators()
        setupCurrentIndicator(0)
        introSliderViewPaper.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                setupCurrentIndicator(position)
            }
        })
        nextButton.safeClick {
            if (introSliderViewPaper.currentItem + 1 > introSlideAdapter.itemCount) {
                introSliderViewPaper.currentItem += 1
            } else {
                startActivity(Intent(applicationContext, MainActivity::class.java))
            }
        }
        skipText.safeClick {
            startActivity(Intent(applicationContext, MainActivity::class.java))
        }
    }

    private fun setupIntroSlide() {
        introSliderViewPaper.adapter = introSlideAdapter
        introSlideAdapter.submitList(
            listOf(
                ItemViewHolder(
                    IntroSlide(
                        "Sample 1",
                        "Sunlight is the light  and energy that comes from the Sun.",
                        R.drawable.ic_logo
                    )
                ),
                ItemViewHolder(
                    IntroSlide(
                        "Sample 2",
                        "Sunlight is the light  and energy that comes from the Sun.",
                        R.drawable.ic_logo
                    )
                ),
                ItemViewHolder(
                    IntroSlide(
                        "Sample 3",
                        "Sunlight is the light  and energy that comes from the Sun.",
                        R.drawable.ic_logo
                    )
                )
            )
        )
    }

    private fun setupIndicators() {
        val indicators = arrayOfNulls<ImageView>(introSlideAdapter.itemCount)
        val layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        layoutParams.setMargins(8, 0, 8, 0)
        for (i in indicators.indices) {
            indicators[i] = ImageView(applicationContext)
            indicators[i].apply {
                this?.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
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
                        applicationContext,
                        R.drawable.indicator_active
                    )
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_inactive
                    )
                )

            }
        }
    }
}
