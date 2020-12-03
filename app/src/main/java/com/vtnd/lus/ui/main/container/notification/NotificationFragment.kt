package com.vtnd.lus.ui.main.container.notification

import android.view.LayoutInflater
import com.vtnd.lus.R
import com.vtnd.lus.base.BaseFragment
import com.vtnd.lus.databinding.FragmentNotificationBinding
import com.vtnd.lus.shared.extensions.initToolbar
import com.vtnd.lus.shared.extensions.initToolbarBase
import org.koin.androidx.viewmodel.ext.android.viewModel

class NotificationFragment : BaseFragment<FragmentNotificationBinding,
        NotificationViewModel>() {

    override val viewModel: NotificationViewModel by viewModel()

    override fun inflateViewBinding(inflater: LayoutInflater) =
        FragmentNotificationBinding.inflate(inflater)

    override fun initialize() {
        showChildNoDataFragment(R.id.notificationLayout)
        initToolbar(
                getString(R.string.notification),
                iconRight = R.drawable.ic_baseline_settings_24,
                isShowIconLeft = true
        ) {

        }
    }

    override fun registerLiveData() {
        super.registerLiveData()
    }

    companion object {
        fun newInstance() = NotificationFragment()
    }
}
