package com.dicoding.storyapp.ui.maps

import android.content.res.Resources
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.dicoding.storyapp.R
import com.dicoding.storyapp.constants.Constants
import com.dicoding.storyapp.custom_view.CustomAlertDialog
import com.dicoding.storyapp.databinding.FragmentMapsBinding
import com.dicoding.storyapp.model.Story
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment() {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    private val mapsViewModel: MapViewModel by activityViewModels()
    private val boundsBuilder = LatLngBounds.Builder()

    private val callback = OnMapReadyCallback { googleMap ->
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.uiSettings.isIndoorLevelPickerEnabled = true
        googleMap.uiSettings.isCompassEnabled = true
        googleMap.uiSettings.isMapToolbarEnabled = true

        val dummyLocation = LatLng(Constants.DICODING_LATITUDE, Constants.DICODING_LONGITUDE)
        googleMap.addMarker(
            MarkerOptions()
                .position(dummyLocation)
                .title("Marker in dummyLocation")
        )
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dummyLocation, 15f))

        setMapStyle(googleMap)
        mapsViewModel.listStories.observe(viewLifecycleOwner) {
            showMarker(it, googleMap)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        mapsViewModel.isError.observe(viewLifecycleOwner) {
            errorHandler(it)
        }

        mapsViewModel.isLoading.observe(viewLifecycleOwner) {
            shodLoading(it)
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun showMarker(listStory: List<Story>, googleMap: GoogleMap) {
        listStory.forEach { story ->
            val latLng = LatLng(story.lat, story.lon)
            googleMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(story.createdAt)
                    .snippet(StringBuilder("created: ")
                        .append(story.createdAt.subSequence(11, 16).toString())
                        .toString()
                    )
            )
            boundsBuilder.include(latLng)
        }
    }

    private fun errorHandler(isError: Boolean) {
        if (isError) {
            CustomAlertDialog(binding.root.context, R.string.error_message, R.drawable.error).show()
        }
    }

    private fun setMapStyle(googleMap: GoogleMap) {
        try {
            val success =
                googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(binding.root.context, R.raw.map_style))
            if (!success) {
                CustomAlertDialog(binding.root.context, R.string.error_message, R.drawable.error).show()
            }
        } catch (exception: Resources.NotFoundException) {
            CustomAlertDialog(binding.root.context, R.string.error_message, R.drawable.error).show()
        }
    }

    private fun shodLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBarMap.visibility = View.VISIBLE
            binding.root.visibility = View.INVISIBLE
        } else {
            binding.progressBarMap.visibility = View.INVISIBLE
            binding.root.visibility = View.VISIBLE
        }
    }
}