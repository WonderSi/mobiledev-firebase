package com.example.mobiledev_firebase.about

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.mobiledev_firebase.databinding.FragmentAboutBinding
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.runtime.image.ImageProvider

class AboutFragment : Fragment() {

    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!

    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        openNavigation()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupMap()

        binding.btnBuildRoute.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                openNavigation()
            } else {
                locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    private fun setupMap() {
        val map = binding.mapView.mapWindow.map
        val officePoint = Point(OFFICE_LAT, OFFICE_LNG)

        map.move(CameraPosition(officePoint, 15f, 0f, 0f))

        val placemark = map.mapObjects.addPlacemark(officePoint)
        placemark.setIcon(
            ImageProvider.fromResource(requireContext(), android.R.drawable.ic_menu_mylocation)
        )
    }

    private fun openNavigation() {
        val yandexUri = Uri.parse(
            "yandexmaps://maps.yandex.ru/?rtext=~$OFFICE_LAT,$OFFICE_LNG&rtt=auto"
        )
        try {
            startActivity(Intent(Intent.ACTION_VIEW, yandexUri))
        } catch (e: ActivityNotFoundException) {
            val geoUri = Uri.parse("geo:$OFFICE_LAT,$OFFICE_LNG?q=$OFFICE_LAT,$OFFICE_LNG")
            startActivity(Intent(Intent.ACTION_VIEW, geoUri))
        }
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        binding.mapView.onStart()
    }

    override fun onStop() {
        binding.mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val OFFICE_LAT = 55.351907
        private const val OFFICE_LNG = 86.091051
    }
}
