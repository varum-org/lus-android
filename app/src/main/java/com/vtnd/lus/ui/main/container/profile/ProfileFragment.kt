package com.vtnd.lus.ui.main.container.profile

import android.view.LayoutInflater
import android.widget.Toast
import com.vtnd.lus.base.BaseFragment
import com.vtnd.lus.databinding.FragmentProfileBinding
import com.vtnd.lus.shared.liveData.observeLiveData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileViewModel>() {

    override val viewModel: ProfileViewModel by viewModel()

    override fun inflateViewBinding(inflater: LayoutInflater) =
        FragmentProfileBinding.inflate(inflater)

    override fun initialize() {}

    @ExperimentalCoroutinesApi
    override fun registerLiveData() = with(viewModel) {
        super.registerLiveData()
        userProfile.observeLiveData(viewLifecycleOwner) {
            it?.let {
                Toast.makeText(activity, "${it.email}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        fun newInstance() = ProfileFragment()
    }
}
