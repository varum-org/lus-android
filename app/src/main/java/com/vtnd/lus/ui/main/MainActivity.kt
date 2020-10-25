package com.vtnd.lus.ui.main

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.vtnd.lus.R
import com.vtnd.lus.shared.extensions.addFragmentToActivity
import com.vtnd.lus.ui.main.container.ContainerFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.runBlocking
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        runBlocking {
            logoSplash()
            addFragmentToActivity(R.id.container, ContainerFragment.newInstance(), false)
        }
    }

    private fun logoSplash() {
        AnimatorSet().run {
            play(
                ObjectAnimator.ofFloat(logoImageView, "scaleX", 1.0f, 2.0f)
                    .also {
                        interpolator = AccelerateDecelerateInterpolator()
                        duration = 1500
                    }).with(
                ObjectAnimator.ofFloat(
                    logoImageView, "scaleY", 1.0f, 2.0f
                ).also {
                    interpolator = AccelerateDecelerateInterpolator()
                    duration = 1500
                }).with(
                ObjectAnimator.ofFloat(
                    logoImageView, "alpha", 1.0f, 0f
                ).also {
                    interpolator = AccelerateDecelerateInterpolator()
                    duration = 1200
                })
            start()
        }
    }
}
