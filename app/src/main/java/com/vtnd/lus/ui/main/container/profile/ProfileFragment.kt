package com.vtnd.lus.ui.main.container.profile

import android.view.LayoutInflater
import com.vtnd.lus.R
import com.vtnd.lus.base.BaseFragment
import com.vtnd.lus.databinding.FragmentProfileBinding
import com.vtnd.lus.shared.extensions.initToolbar
import com.vtnd.lus.shared.liveData.observeLiveData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileViewModel>() {

    override val viewModel: ProfileViewModel by viewModel()

    override fun inflateViewBinding(inflater: LayoutInflater) =
        FragmentProfileBinding.inflate(inflater)

    override fun initialize() {
        initToolbar(
            getString(R.string.profile),
            iconRight = R.drawable.ic_baseline_settings_24,
            isShowIconLeft = true
        ) {}
    }

    @ExperimentalCoroutinesApi
    override fun registerLiveData() = with(viewModel) {
        super.registerLiveData()
        userProfile.observeLiveData(viewLifecycleOwner) {}
    }

    companion object {
        fun newInstance() = ProfileFragment()
    }
}
