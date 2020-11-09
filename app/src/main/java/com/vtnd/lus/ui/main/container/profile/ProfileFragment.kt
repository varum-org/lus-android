package com.vtnd.lus.ui.main.container.profile

import android.view.LayoutInflater
import com.vtnd.lus.base.BaseFragment
import com.vtnd.lus.databinding.FragmentProfileBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : BaseFragment<FragmentProfileBinding,
        ProfileViewModel>() {

    override val viewModel: ProfileViewModel by viewModel()

    override fun inflateViewBinding(inflater: LayoutInflater) =
        FragmentProfileBinding.inflate(inflater)

    override fun initialize() {}

    override fun registerLiveData() {
        super.registerLiveData()
    }

    companion object {
        fun newInstance() = ProfileFragment()
    }
}
