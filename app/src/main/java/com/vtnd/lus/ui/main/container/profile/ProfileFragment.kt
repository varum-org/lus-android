package com.vtnd.lus.ui.main.container.profile

import android.view.LayoutInflater
import com.vtnd.lus.R
import com.vtnd.lus.base.BaseFragment
import com.vtnd.lus.data.model.User
import com.vtnd.lus.databinding.FragmentProfileBinding
import com.vtnd.lus.shared.decoration.FlexibleGridSpacingItemDecoration
import com.vtnd.lus.shared.extensions.setTextDefaultValue
import com.vtnd.lus.shared.liveData.observeLiveData
import com.vtnd.lus.ui.main.container.profile.adapter.MenuIdolAdapter
import com.vtnd.lus.ui.main.container.profile.adapter.MenuSettingAdapter
import com.vtnd.lus.ui.main.container.profile.adapter.MenuUserAdapter
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileViewModel>() {

    private val menuIdolAdapter by lazy { MenuIdolAdapter {} }
    private val menuUserAdapter by lazy { MenuUserAdapter {} }
    private val menuSettingAdapter by lazy { MenuSettingAdapter {} }

    override val viewModel: ProfileViewModel by viewModel()

    override fun inflateViewBinding(inflater: LayoutInflater) =
        FragmentProfileBinding.inflate(inflater)

    override fun initialize() {
        setupRecyclerView()
    }

    private fun binDataToView(user: User?) {
        userNameText.setTextDefaultValue(user?.userName)
    }

    private fun setupRecyclerView() {
        menuIdolRecyclerView.apply {
            setHasFixedSize(true)
            adapter = menuIdolAdapter
        }
        menuUserRecyclerView.apply {
            setHasFixedSize(true)
            adapter = menuUserAdapter
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
        menuSettingRecyclerView.apply {
            setHasFixedSize(true)
            adapter = menuSettingAdapter
        }
    }

    @ExperimentalCoroutinesApi
    override fun registerLiveData() = with(viewModel) {
        super.registerLiveData()
        userProfile.observeLiveData(viewLifecycleOwner, ::binDataToView)
        menuIdolLiveData.observeLiveData(viewLifecycleOwner, menuIdolAdapter::submitList)
        menuUserLiveData.observeLiveData(viewLifecycleOwner, menuUserAdapter::submitList)
        menuSettingLiveData.observeLiveData(viewLifecycleOwner, menuSettingAdapter::submitList)
    }

    companion object {
        fun newInstance() = ProfileFragment()
    }
}
