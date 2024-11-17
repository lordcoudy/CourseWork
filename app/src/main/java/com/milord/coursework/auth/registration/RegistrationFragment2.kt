package com.milord.coursework.auth.registration

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.milord.coursework.MainActivity
import com.milord.coursework.R
import com.milord.coursework.data.UserData
import com.milord.coursework.databinding.FragmentRegistration2Binding
import com.milord.coursework.utils.UserViewModel
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.location.FilteringMode
import com.yandex.mapkit.location.Location
import com.yandex.mapkit.location.LocationListener
import com.yandex.mapkit.location.LocationManager
import com.yandex.mapkit.location.LocationStatus
import com.yandex.mapkit.location.Purpose
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import java.util.Locale

class RegistrationFragment2 : Fragment() {
    private lateinit var binding: FragmentRegistration2Binding
    private lateinit var mapView: MapView
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener
    private var curLocation: Point? = null
    private val DESIRED_ACCURACY: Double = 0.0
    private val MINIMAL_TIME: Long = 1000
    private val MINIMAL_DISTANCE: Double = 1.0
    private val USE_IN_BACKGROUND: Boolean = false
    private val COMFORTABLE_ZOOM_LEVEL: Float = 18F
    private val userViewModel = UserViewModel.getInstance()
    private var user: UserData? = null

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.setApiKey("MAPKIT_API_KEY")
        MapKitFactory.initialize(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistration2Binding.inflate(layoutInflater)
        mapView = binding.mapview
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        user = userViewModel.userData.value
        locationManager = MapKitFactory.getInstance().createLocationManager()
        locationListener = object : LocationListener {
            override fun onLocationUpdated(location: Location) {
                if (curLocation == null) {
                    moveCamera(location.position, COMFORTABLE_ZOOM_LEVEL)
                }
                curLocation = location.position
                val mGeocoder = Geocoder(requireContext(), Locale.getDefault())
                var addressString = ""
                // Reverse-Geocoding starts
                val addresses = mGeocoder.getFromLocation(curLocation!!.latitude, curLocation!!.longitude, 1)
                val address = addresses?.get(0)
                val sb = StringBuilder()
                if (address != null) {
                    for (i in 0 until address.maxAddressLineIndex) {
                        sb.append(address.getAddressLine(i)).append("\n")
                    }

                    if (address.premises != null)
                        sb.append(address.premises).append(", ")

                    sb.append(address.subAdminArea).append("\n")
                    sb.append(address.locality).append(", ")
                    sb.append(address.adminArea).append(", ")
                    sb.append(address.countryName).append(", ")
                    sb.append(address.postalCode)
                }

                addressString = sb.toString()
                user?.setAddress(addressString)
                user?.let { userViewModel.updateUser(it) }
            }

            override fun onLocationStatusUpdated(locationStatus: LocationStatus) {
                if (locationStatus == LocationStatus.NOT_AVAILABLE) {
                    println("Location is not available")
                }
            }
        }

        val buttonNext = binding.nextButton2
        val buttonBack = binding.backButton2
        val addressText = binding.editTextAddress

        userViewModel.userData.observe(viewLifecycleOwner) { userData ->
            user = userData
            addressText.setText(user?.getAddress())
        }

        buttonNext.setOnClickListener {
            user?.setAddress(addressText.text.toString())
            user?.let { it1 -> userViewModel.updateUser(it1) }
            findNavController().navigate(R.id.action_registrationFragment2_to_registrationFragment3)
        }

        buttonBack.setOnClickListener {
            user?.setAddress(addressText.text.toString())
            user?.let { it1 -> userViewModel.updateUser(it1) }
            findNavController().navigate(R.id.action_registrationFragment2_to_registrationFragment1)
        }

        checkLocationPermission()
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
        if (hasLocationPermission()) {
            subscribeToLocationUpdate()
        }
    }

    override fun onStop() {
        super.onStop()
        MapKitFactory.getInstance().onStop()
        locationManager.unsubscribe(locationListener)
        mapView.onStop()
    }

    private fun checkLocationPermission() {
        if (!hasLocationPermission()) {
            requestLocationPermission()
        }
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                subscribeToLocationUpdate()
            } else {
                Toast.makeText(requireContext(), "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun subscribeToLocationUpdate() {
        locationManager.subscribeForLocationUpdates(
            DESIRED_ACCURACY,
            MINIMAL_TIME,
            MINIMAL_DISTANCE,
            USE_IN_BACKGROUND,
            FilteringMode.OFF,
            Purpose.GENERAL,
            locationListener
        )
    }

    private fun moveCamera(point: Point, zoom: Float) {
        mapView.mapWindow.map.move(
            CameraPosition(point, zoom, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 1F),
            null
        )
    }
}