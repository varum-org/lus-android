package com.vtnd.lus.ui.main.container.registerIdol.addressIdol

import android.view.LayoutInflater
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.compat.ui.PlaceAutocompleteFragment
import com.google.android.libraries.places.compat.ui.PlaceSelectionListener
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.compat.Place
import com.vtnd.lus.R
import com.vtnd.lus.base.BaseFragment
import com.vtnd.lus.databinding.FragmentAddressIdolBinding
import com.vtnd.lus.ui.main.container.registerIdol.RegisterIdolViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

@Suppress("DEPRECATION")
class AddressIdolFragment : BaseFragment<FragmentAddressIdolBinding,
        RegisterIdolViewModel>(), OnMapReadyCallback {
    private var map: GoogleMap? = null
    private val sydney = LatLng(-8.579892, 116.095239)

    override val viewModel: RegisterIdolViewModel by viewModel()

    override fun inflateViewBinding(inflater: LayoutInflater) =
        FragmentAddressIdolBinding.inflate(inflater)

    override fun initialize() {
        val mapFragment =
            requireActivity().fragmentManager.findFragmentById(R.id.map) as MapFragment?
        mapFragment?.getMapAsync(this)
        setupAutoCompleteFragment()
    }

    override fun registerLiveData() {
        super.registerLiveData()
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap
        map?.apply {
            moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 8.5f))
            addMarker(
                MarkerOptions()
                    .position(sydney)
                    .title("kodetr")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
            )
        }
    }

    private fun setupAutoCompleteFragment() {
        val autocompleteFragment =
            requireActivity().fragmentManager.findFragmentById(R.id.place_autocomplete_fragment) as PlaceAutocompleteFragment?
        autocompleteFragment?.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place?) {
            }

            override fun onError(status: Status?) {
                Timber.i("Error%s", status?.statusMessage)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        map?.clear()
    }
    companion object {
        fun newInstance() = AddressIdolFragment()
    }
}
