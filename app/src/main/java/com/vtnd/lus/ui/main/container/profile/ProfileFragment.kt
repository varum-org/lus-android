package com.vtnd.lus.ui.main.container.profile

import android.content.Intent
import android.view.LayoutInflater
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.vtnd.lus.R
import com.vtnd.lus.base.BaseFragment
import com.vtnd.lus.data.model.Idol
import com.vtnd.lus.data.repository.source.remote.api.response.IdolResponse
import com.vtnd.lus.databinding.FragmentProfileBinding
import com.vtnd.lus.di.GlideApp
import com.vtnd.lus.shared.AnimateType
import com.vtnd.lus.shared.TYPE_FOOTER
import com.vtnd.lus.shared.constants.Constants
import com.vtnd.lus.shared.decoration.FlexibleGridSpacingItemDecoration
import com.vtnd.lus.shared.extensions.*
import com.vtnd.lus.shared.liveData.observeLiveData
import com.vtnd.lus.ui.auth.AuthActivity
import com.vtnd.lus.ui.main.container.addCard.AddCoinFragment
import com.vtnd.lus.ui.main.container.history.HistoryFragment
import com.vtnd.lus.ui.main.container.historyIdol.HistoryIdolFragment
import com.vtnd.lus.ui.main.container.profile.adapter.MenuIdolAdapter
import com.vtnd.lus.ui.main.container.profile.adapter.MenuSettingAdapter
import com.vtnd.lus.ui.main.container.profile.adapter.MenuUserAdapter
import com.vtnd.lus.ui.main.container.registerIdol.RegisterIdolFragment
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileViewModel>(),
    SwipeRefreshLayout.OnRefreshListener {

    @ExperimentalCoroutinesApi
    private val menuIdolAdapter by lazy {
        MenuIdolAdapter {
            when (it) {
                is Idol -> {
                }
                else -> {
                    viewModel.checkLogin { isLogin ->
                        if (isLogin)
                            replaceFragment(
                                R.id.container,
                                RegisterIdolFragment.newInstance(),
                                addToBackStack = true,
                                animateType = AnimateType.SLIDE_TO_RIGHT
                            )
                        else activity?.apply {
                            showLoading(true)
                            delayTask({
                                showLoading(false)
                                startActivity(Intent(this, AuthActivity::class.java))
                                overridePendingTransition(R.anim.bottom_up, R.anim.nothing)
                            }, 800)
                        }
                    }
                }
            }
        }
    }
    private val menuUserAdapter by lazy {
        MenuUserAdapter {
            when (it.title) {
                R.string.invite_friends, R.string.feedback -> {
                }
                else -> {
                    viewModel.checkLogin { isLogin ->
                        if (isLogin) {
                            handleItemOfUser(it.title)
                        } else activity?.apply {
                            showLoading(true)
                            delayTask({
                                showLoading(false)
                                startActivity(Intent(this, AuthActivity::class.java))
                                overridePendingTransition(R.anim.bottom_up, R.anim.nothing)
                            }, 800)
                        }
                    }
                }
            }
        }
    }

    private fun handleItemOfUser(id: Int?) {
        when (id) {
            R.string.wallet -> {
                replaceFragment(
                    containerId = R.id.container,
                    fragment = AddCoinFragment.newInstance(),
                    addToBackStack = true
                )
            }
            R.string.image_gallery -> {

            }
            R.string.bill -> {

            }
            R.string.history_idol-> {
                replaceFragment(
                    R.id.container,
                    HistoryIdolFragment.newInstance(),
                    addToBackStack = true,
                    animateType = AnimateType.SLIDE_TO_RIGHT
                )
            }
        }
    }

    private val menuSettingAdapter by lazy {
        MenuSettingAdapter {
            when (it.title) {
                R.string.change_password -> {
                    viewModel.checkLogin { isLogin ->
                        if (isLogin) {
                        } else activity?.apply {
                            showLoading(true)
                            delayTask({
                                showLoading(false)
                                startActivity(Intent(this, AuthActivity::class.java))
                                overridePendingTransition(R.anim.bottom_up, R.anim.nothing)
                            }, 800)
                        }
                    }
                }
                R.string.history -> {
                    viewModel.checkLogin { isLogin ->
                        if (isLogin) {
                            replaceFragment(
                                R.id.container,
                                HistoryFragment.newInstance(),
                                addToBackStack = true,
                                animateType = AnimateType.SLIDE_TO_RIGHT
                            )
                        } else activity?.apply {
                            showLoading(true)
                            delayTask({
                                showLoading(false)
                                startActivity(Intent(this, AuthActivity::class.java))
                                overridePendingTransition(R.anim.bottom_up, R.anim.nothing)
                            }, 800)
                        }
                    }
                }
                R.string.log_out -> {
                    requireActivity().showAlertDialog {
                        title("Logout")
                        message("Are you sure you want to logout?")
                        iconId(R.drawable.ic_logout)
                        cancelable(true)
                        negativeAction("Cancel") { _, _ -> }
                        positiveAction("OK") { _, _ ->
                            viewModel.logout()
                        }
                    }
                }
            }
        }
    }

    override val viewModel: ProfileViewModel by viewModel()

    override fun inflateViewBinding(inflater: LayoutInflater) =
        FragmentProfileBinding.inflate(inflater)

    override fun initialize() {
        initView()
        setupRecyclerView()
    }

    private fun initView() {
        profileRefresh.setOnRefreshListener(this)
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
            idolResponse.idol?.let { idol ->
                viewModel.postMenuIdol(idol)
            } ?: viewModel.postMenuIdol(Any(), TYPE_FOOTER)
        } ?: apply {
            userImage.randomAvatar()
            viewModel.postMenuIdol(Any(), TYPE_FOOTER)
        }
    }

    private fun setupRecyclerView() {
        menuIdolRecyclerView.apply {
            adapter = menuIdolAdapter
        }
        menuUserRecyclerView.apply {
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
            adapter = menuSettingAdapter
        }
    }

    @ExperimentalCoroutinesApi
    override fun registerLiveData() = with(viewModel) {
        super.registerLiveData()
        userProfile.observeLiveData(viewLifecycleOwner) {
            binDataToView(it)
            profileRefresh.isRefreshing = false
        }
        menuIdolLiveData.observeLiveData(viewLifecycleOwner, menuIdolAdapter::submitList)
        menuUserLiveData.observeLiveData(viewLifecycleOwner, menuUserAdapter::submitList)
        menuSettingLiveData.observeLiveData(viewLifecycleOwner, menuSettingAdapter::submitList)
        logoutLiveData.observeLiveData(viewLifecycleOwner) { requireContext().toast("Logout success") }
    }

    companion object {
        fun newInstance() = ProfileFragment()
    }

    override fun onRefresh() {
        viewModel.getUser() {
            profileRefresh.isRefreshing = false
        }
    }
}
