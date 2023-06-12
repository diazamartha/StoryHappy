package com.example.storyhappy.presentation.maps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.storyhappy.R
import com.example.storyhappy.data.Result
import com.example.storyhappy.data.source.remote.response.ListStoryItem
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.storyhappy.databinding.ActivityMapsBinding
import com.example.storyhappy.presentation.main.MainActivity
import com.example.storyhappy.presentation.main.MainViewModel
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private var _binding: ActivityMapsBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel: MainViewModel by viewModel()

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.clear()
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        mainViewModel.getToken().observe(this@MapsActivity) { token ->
            mainViewModel.getStoriesWithLocation(token).observe(this) { mapState ->
                when (mapState) {
                    is Result.Success -> {
                        val storyList = mapState.data.listStory.map {
                            ListStoryItem(
                                id = it.id,
                                name = it.name,
                                description = it.description,
                                photoUrl = it.photoUrl,
                                createdAt = it.createdAt,
                                lat = it.lat,
                                lon = it.lon
                            )
                        }
                        showStoryWithLocation(storyList)
                        showLoading(false)
                    }

                    is Result.Loading -> {
                        showLoading(true)
                    }

                    is Result.Error -> {
                        showLoading(false)
                        showToast(mapState.message)
                    }

                    else -> {}
                }

            }
        }
    }

    private fun showStoryWithLocation(response: List<ListStoryItem>) {

        val indonesiaLatLng = LatLng(-0.7893, 113.9213)
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(indonesiaLatLng, 2f))

        lifecycleScope.launch {
            for ((index, story) in response.reversed().withIndex()) {
                val latLng = LatLng(story.lat, story.lon)
                val bitmap = withContext(Dispatchers.IO) {
                    Glide.with(this@MapsActivity)
                        .asBitmap()
                        .load(story.photoUrl)
                        .override(64, 64)
                        .circleCrop()
                        .submit()
                        .get()
                }
                val bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap)

                mMap.addMarker(
                    MarkerOptions()
                        .position(latLng)
                        .title(story.name)
                        .icon(bitmapDescriptor)
                        .zIndex(index.toFloat())
                )
            }
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this@MapsActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        navigateToMainActivity()
    }

    private fun showToast(message: String) {
        Toast.makeText(this@MapsActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        val visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.progressBar.visibility = visibility
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}