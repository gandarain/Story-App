package com.dicoding.storyapp.ui.maps

import android.content.res.Resources
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.dicoding.storyapp.R
import com.dicoding.storyapp.constants.Constants
import com.dicoding.storyapp.custom_view.CustomAlertDialog
import com.dicoding.storyapp.data.Result
import com.dicoding.storyapp.databinding.FragmentMapsBinding
import com.dicoding.storyapp.model.Story
import com.dicoding.storyapp.utils.ViewModelFactory
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
    private lateinit var factory: ViewModelFactory
    private val mapsViewModel: MapViewModel by viewModels { factory }
    private val boundsBuilder = LatLngBounds.Builder()

    private val callback = OnMapReadyCallback { googleMap ->
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.uiSettings.isIndoorLevelPickerEnabled = true
        googleMap.uiSettings.isCompassEnabled = true
        googleMap.uiSettings.isMapToolbarEnabled = true

        setupViewModel()

        val dummyLocation = LatLng(Constants.DICODING_LATITUDE, Constants.DICODING_LONGITUDE)
        googleMap.addMarker(
            MarkerOptions()
                .position(dummyLocation)
                .title("Marker in dummyLocation")
        )
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dummyLocation, 15f))

        setMapStyle(googleMap)
        getStoryWithLocation(googleMap)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    private fun setupViewModel() {
        factory = ViewModelFactory.getInstance(binding.root.context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun getStoryWithLocation(googleMap: GoogleMap) {
        mapsViewModel.getStories().observe(this) { result ->
            if (result != null) {
                when(result) {
                    is Result.Loading -> {
                        loadingHandler(true)
                    }
                    is Result.Error -> {
                        loadingHandler(false)
                        errorHandler()
                    }
                    is Result.Success -> {
                        loadingHandler(false)
                        showMarker(result.data.listStory, googleMap)
                    }
                }
            }
        }
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

    private fun errorHandler() {
        CustomAlertDialog(binding.root.context, R.string.error_message, R.drawable.error).show()
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

    private fun loadingHandler(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBarMap.visibility = View.VISIBLE
            binding.root.visibility = View.INVISIBLE
        } else {
            binding.progressBarMap.visibility = View.INVISIBLE
            binding.root.visibility = View.VISIBLE
        }
    }
}