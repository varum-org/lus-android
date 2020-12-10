package com.vtnd.lus.ui.main.container.registerIdol

import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import com.vtnd.lus.R
import com.vtnd.lus.base.BaseFragment
import com.vtnd.lus.databinding.FragmentRegisterIdolBinding
import com.vtnd.lus.shared.extensions.initToolbarBase
import com.vtnd.lus.shared.extensions.invisible
import com.vtnd.lus.shared.extensions.setupDismissKeyBoard
import com.vtnd.lus.shared.extensions.visible
import com.vtnd.lus.ui.main.container.adapter.ContainerViewPagerAdapter
import com.vtnd.lus.ui.main.container.registerIdol.addressIdol.AddressIdolFragment
import com.vtnd.lus.ui.main.container.registerIdol.infomationIdol.InformationIdolFragment
import com.vtnd.lus.ui.main.container.registerIdol.serviceIdol.ServiceIdolFragment
import kotlinx.android.synthetic.main.fragment_register_idol.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterIdolFragment : BaseFragment<FragmentRegisterIdolBinding, RegisterIdolViewModel>() {

    private val addressFragment by lazy { AddressIdolFragment.newInstance() }
    private val informationFragment by lazy { InformationIdolFragment.newInstance() }
    private val serviceFragment by lazy { ServiceIdolFragment.newInstance() }

    override val viewModel: RegisterIdolViewModel by viewModel()

    override fun inflateViewBinding(inflater: LayoutInflater) =
        FragmentRegisterIdolBinding.inflate(inflater)

    override fun initialize() {
        setupDismissKeyBoard(activity, registerIdolLayout)
        initToolbarBase(
            getString(R.string.edit_idol_profile),
            isShowIconLeft = true,
            iconRight = R.drawable.ic_save
        ) {  }
        setupViewPaper()
    }

    override fun registerLiveData() {
        super.registerLiveData()
    }

    override fun onStop() {
        onHideSoftKeyBoard()
        super.onStop()
    }

    private fun setupViewPaper() {
        addIdolViewPaper.apply {
            offscreenPageLimit = 3
            val fragments = listOf<Fragment>(informationFragment, addressFragment, serviceFragment)
            val adapterViewPaper = ContainerViewPagerAdapter(childFragmentManager, fragments)
            adapter = adapterViewPaper
            currentItem = 0
            backButton.invisible()
            backButton.setOnClickListener {
                nextButton.visible()
                when {
                    currentItem - 1 == 0 -> {
                        backButton.invisible()
                        currentItem -= 1
                    }
                    currentItem - 1 > 0 -> {
                        currentItem -= 1
                    }
                }
            }
            nextButton.setOnClickListener {
                backButton.visible()
                when {
                    currentItem + 1 == adapterViewPaper.count - 1 -> {
                        nextButton.invisible()
                        currentItem += 1
                    }
                    currentItem + 1 < adapterViewPaper.count - 1 -> {
                        currentItem += 1
                    }
                }
            }
        }
    }

    companion object {
        fun newInstance() = RegisterIdolFragment()
    }
}
