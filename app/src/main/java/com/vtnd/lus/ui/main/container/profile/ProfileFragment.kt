package com.vtnd.lus.ui.main.container.profile

import android.view.LayoutInflater
import android.widget.GridLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.vtnd.lus.R
import com.vtnd.lus.base.BaseFragment
import com.vtnd.lus.data.repository.source.remote.api.response.IdolResponse
import com.vtnd.lus.databinding.FragmentProfileBinding
import com.vtnd.lus.di.GlideApp
import com.vtnd.lus.shared.constants.Constants
import com.vtnd.lus.shared.decoration.FlexibleGridSpacingItemDecoration
import com.vtnd.lus.shared.extensions.randomAvatar
import com.vtnd.lus.shared.extensions.setTextDefaultValue
import com.vtnd.lus.shared.liveData.observeLiveData
import com.vtnd.lus.ui.main.container.profile.adapter.MenuIdolAdapter
import com.vtnd.lus.ui.main.container.profile.adapter.MenuSettingAdapter
import com.vtnd.lus.ui.main.container.profile.adapter.MenuUserAdapter
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

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

    private fun binDataToView(user: IdolResponse?) {
        userNameText.setTextDefaultValue(user?.user?.userName)
        user?.let { idolResponse ->
            idolResponse.user.imagePath?.let {
                GlideApp.with(userImage)
                    .load(Constants.BASE_IMAGE_URL + it)
                    .placeholder(R.color.pink_50)
                    .error(R.color.red_a400)
                    .dontAnimate()
                    .into(userImage)
            } ?: userImage.randomAvatar()
            idolResponse.idol?.let {idol->
                viewModel.postMenuIdol(idol)
            }
        } ?: userImage.randomAvatar()
    }

    private fun setupRecyclerView() {
        menuIdolRecyclerView.apply {
            setHasFixedSize(false)
            adapter = menuIdolAdapter
        }
        menuUserRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(requireContext(),2,GridLayoutManager.VERTICAL,false)
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
