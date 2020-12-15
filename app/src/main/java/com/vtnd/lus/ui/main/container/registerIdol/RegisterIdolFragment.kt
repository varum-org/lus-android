package com.vtnd.lus.ui.main.container.registerIdol

import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.vtnd.lus.R
import com.vtnd.lus.base.BaseFragment
import com.vtnd.lus.databinding.FragmentRegisterIdolBinding
import com.vtnd.lus.shared.extensions.*
import com.vtnd.lus.shared.liveData.observeLiveData
import com.vtnd.lus.ui.main.container.adapter.ContainerViewPagerAdapter
import com.vtnd.lus.ui.main.container.registerIdol.addressIdol.AddressIdolFragment
import com.vtnd.lus.ui.main.container.registerIdol.imageGallery.ImageGalleryFragment
import com.vtnd.lus.ui.main.container.registerIdol.infomationIdol.InformationIdolFragment
import com.vtnd.lus.ui.main.container.registerIdol.serviceIdol.ServiceIdolFragment
import kotlinx.android.synthetic.main.fragment_register_idol.*
import kotlinx.android.synthetic.main.layout_toolbar_base.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterIdolFragment : BaseFragment<FragmentRegisterIdolBinding, RegisterIdolViewModel>(),
    ViewPager.OnPageChangeListener {

    private val addressFragment by lazy { AddressIdolFragment.newInstance() }
    private val informationFragment by lazy { InformationIdolFragment.newInstance() }
    private val serviceFragment by lazy { ServiceIdolFragment.newInstance() }
    private val imageGalleryFragment by lazy { ImageGalleryFragment.newInstance() }

    override val viewModel: RegisterIdolViewModel by viewModel()

    override fun inflateViewBinding(inflater: LayoutInflater) =
        FragmentRegisterIdolBinding.inflate(inflater)

    override fun initialize() {
        setupDismissKeyBoard(activity, registerIdolLayout)
        initToolbarBase(
            getString(R.string.information_idol),
            isShowIconLeft = true,
            iconRight = R.drawable.ic_heart_bold,
            color = R.color.red_a400
        ) { viewModel.registerIdol() }
        setupViewPaper()
    }

    override fun registerLiveData() = with(viewModel) {
        super.registerLiveData()
        isShowButton.observeLiveData(viewLifecycleOwner) {
            nextButton.isEnabled = it
        }
        isShowButtonRegister.observeLiveData(viewLifecycleOwner) {
            rightImageBaseButton.apply {
                if (it) setTint(R.color.red_a400)
                else setTint(R.color.grey_600)
                isEnabled = it
            }
        }
        idolResponse.observeLiveData(viewLifecycleOwner){
            showNotificationAlertDialog {
                icon(R.drawable.ic_check)
                statusMessage(getString(R.string.register_success))
                button(getString(R.string.ok2)){
                    this@RegisterIdolFragment.goBackFragment()
                }
            }
        }
    }

    override fun onStop() {
        onHideSoftKeyBoard()
        super.onStop()
    }

    private fun setupViewPaper() {
        addIdolViewPaper.apply {
            offscreenPageLimit = 3
            val fragments = listOf<Fragment>(informationFragment,
                addressFragment,
                serviceFragment,
                imageGalleryFragment)
            val adapterViewPaper = ContainerViewPagerAdapter(childFragmentManager, fragments)
            adapter = adapterViewPaper
            currentItem = 0
            addOnPageChangeListener(this@RegisterIdolFragment)
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

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
    override fun onPageSelected(position: Int) {
        when (position) {
            0 -> {
                titleToolbarBaseTextView.text = getString(R.string.information_idol)
                viewModel.checkInformation()
            }
            1 -> {
                titleToolbarBaseTextView.text = getString(R.string.location)
                nextButton.isEnabled = true
            }
            2 -> {
                titleToolbarBaseTextView.text = getString(R.string.service_idol)
                viewModel.checkServices()
            }
            3 -> {
                titleToolbarBaseTextView.text = getString(R.string.image_gallery)
                viewModel.checkRegisterIdol()
            }
        }
    }

    override fun onPageScrollStateChanged(state: Int) {}

    companion object {
        fun newInstance() = RegisterIdolFragment()
    }
}
