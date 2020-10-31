package com.vtnd.lus.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import com.vtnd.lus.R
import com.vtnd.lus.base.BaseActivity
import com.vtnd.lus.databinding.ActivityMainBinding
import com.vtnd.lus.shared.extensions.addFragmentToActivity
import com.vtnd.lus.ui.main.container.ContainerFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    override val viewModel: MainViewModel by viewModel()

    override fun inflateViewBinding(inflater: LayoutInflater) =
        ActivityMainBinding.inflate(inflater)

    override fun initialize() {
        addFragmentToActivity(R.id.container, ContainerFragment.newInstance(), false)
    }

    override fun registerLiveData() {
        super.registerLiveData()
    }
}
