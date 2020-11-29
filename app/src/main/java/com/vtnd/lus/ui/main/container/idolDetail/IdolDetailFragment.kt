package com.vtnd.lus.ui.main.container.idolDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.get
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.transition.MaterialContainerTransform
import com.vtnd.lus.R
import com.vtnd.lus.base.BaseFragment
import com.vtnd.lus.base.ItemViewHolder
import com.vtnd.lus.data.repository.source.remote.api.response.IdolResponse
import com.vtnd.lus.databinding.FragmentIdolDetailBinding
import com.vtnd.lus.di.GlideApp
import com.vtnd.lus.shared.constants.Constants.BASE_IMAGE_URL
import com.vtnd.lus.shared.decoration.HorizontalMarginItemDecoration
import com.vtnd.lus.shared.extensions.*
import com.vtnd.lus.shared.liveData.observeLiveData
import com.vtnd.lus.ui.main.container.idolDetail.adapter.CartAdapter
import com.vtnd.lus.ui.main.container.idolDetail.adapter.GalleryAdapter
import com.vtnd.lus.ui.main.container.idolDetail.adapter.ServiceAdapter
import kotlinx.android.synthetic.main.fragment_idol_detail.*
import kotlinx.android.synthetic.main.layout_cart_bottom.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.util.*


class
IdolDetailFragment : BaseFragment<FragmentIdolDetailBinding, IdolDetailViewModel>(),
    View.OnClickListener {

    override val viewModel: IdolDetailViewModel by viewModel()
    private val galleryAdapter = GalleryAdapter {}
    private val cartAdapter = CartAdapter { ActionType, itemData ->
        viewModel.handleHoursService(ActionType, itemData)
    }
    private val serviceAdapter = ServiceAdapter { d, p ->
        viewModel.addServiceToCard(d, p)
    }
    private var calendar = getCalendar()
    private lateinit var idolResponse: IdolResponse
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform()
    }

    override fun inflateViewBinding(inflater: LayoutInflater) =
        FragmentIdolDetailBinding.inflate(inflater)

    override fun initialize() {
        setupDismissKeyBoard(activity, layout)
        arguments?.apply {
            getParcelable<IdolResponse>(ARGUMENT_IDOL)?.let {
                idolImage.transitionName = it.idol.id
                idolResponse = it
                viewModel.addServicesIdol(idolResponse.idol.services)
            }
        }
        setupBottomSheet()
        listenToViews(backImageButton, addFavoriteFAB)
        setupViewPager2()
        setupIndicators()
        setupCurrentIndicator(0)
        idolGalleryViewPager2.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                setupCurrentIndicator(position)
            }
        })
        setupRecyclerView()
        binDataToView()
    }

    override fun registerLiveData() = with(viewModel) {
        super.registerLiveData()
        idolServicesLiveData.observeLiveData(viewLifecycleOwner) {
            Timber.i(it.toString())
            serviceAdapter.submitList(it)
        }
        cardServicesLiveData.observeLiveData(viewLifecycleOwner) {
            badgeText.text = it.size.toString()
            serviceTotal.text = "${it.sumPrice()}"
            cartAdapter.submitList(it)
            bottomSheetBehavior.apply {
                if (it.isNotEmpty()) {
                    if (state == BottomSheetBehavior.STATE_HIDDEN)
                        state = BottomSheetBehavior.STATE_COLLAPSED
                } else {
                    noteText.text = null
                    state = BottomSheetBehavior.STATE_HIDDEN
                }
            }
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.backImageButton -> this.goBackFragment()
            R.id.addFavoriteFAB -> {
            }
        }
    }

    override fun onStop() {
        super.onStop()
        onHideSoftKeyBoard()
    }

    private fun setupBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(cartBottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        headerCartLayout.setOnClickListener {
            if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            else bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {}

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                iconArrowImage.rotation = slideOffset * 180
            }
        })
        startDateImage.setOnClickListener {
            onStartDateClick()
        }
        noteText.setOnClickListener {
            onNoteClick()
        }
    }

    private fun onStartDateClick() {
        showDatePickerAlertDialog(
            calendar.get(Calendar.DAY_OF_MONTH),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.YEAR)
        ) {
            title("Start date")
            leftButton(getString(R.string.cancel))
            rightButton(getString(R.string.apply)) { newDayOfMonth, newMonth, newYear ->
                calendar.set(newYear, newMonth, newDayOfMonth)
                context?.let {
                    startDateText.text = calendar.time.toStringDefaultDate(it)
                }
            }
        }
    }

    private fun onNoteClick() {
        showNoteAlertDialog(noteText.text.toString()) {
            title("Note")
            leftButton(getString(R.string.cancel))
            rightButton(getString(R.string.apply)) { note ->
                noteText.text = note
            }
        }
    }

    private fun binDataToView() = with(idolResponse) {
        GlideApp.with(this@IdolDetailFragment)
            .load(BASE_IMAGE_URL + idol.imageGallery[0])
            .placeholder(R.color.pink_50)
            .error(R.color.red_a400)
            .dontAnimate()
            .into(idolImage)
        idolNameText.text = user?.birthday?.let {
            getString(R.string.nick_name, idol.nickName, it.getAge())
        } ?: idol.nickName
        idolDescriptionText.text = idol.description
        idolLocationText.text = context?.getString(R.string.live_in, "Đà Nẵng")
        idolAddressText.text = getString(R.string.idol_address, idol.address)
        idolRelationshipText.text = idol.relationship
        distinctionText.text = getString(R.string.crush_national_citizen)
    }

    private fun setupViewPager2() {
        idolGalleryViewPager2.apply {
            adapter = galleryAdapter.apply {
                idolResponse.idol.imageGallery.run {
                    subList(1, this.size)
                }.let {
                    submitList(it.map {
                        ItemViewHolder(it)
                    })
                }
            }
            addItemDecoration(HorizontalMarginItemDecoration(context, R.dimen.dp_16))
            offscreenPageLimit = 1
        }
    }

    private fun setupRecyclerView() {
        idolServiceRecyclerView.apply {
            setHasFixedSize(false)
            adapter = serviceAdapter
        }
        cartRecyclerView.apply {
            setHasFixedSize(true)
            adapter = cartAdapter
        }
    }

    private fun setupIndicators() {
        val indicators = arrayOfNulls<ImageView>(galleryAdapter.itemCount)
        val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins(8, 0, 8, 0)
        for (i in indicators.indices) {
            indicators[i] = ImageView(requireContext())
            indicators[i].apply {
                this?.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.indicator_inactive
                    )
                )
                this?.layoutParams = layoutParams
            }
            indicatorsContainer.addView(indicators[i])
        }
    }

    private fun setupCurrentIndicator(index: Int) {
        val childCount = indicatorsContainer.childCount
        for (i in 0 until childCount) {
            val imageView = indicatorsContainer[i] as ImageView
            if (i == index) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.indicator_active
                    )
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.indicator_inactive
                    )
                )
            }
        }
    }

    companion object {
        private const val ARGUMENT_IDOL = "ARGUMENT_IDOL"

        fun newInstance(idol: IdolResponse) = IdolDetailFragment().apply {
            arguments = bundleOf(ARGUMENT_IDOL to idol)
        }
    }
}
