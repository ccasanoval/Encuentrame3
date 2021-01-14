package com.cesoft.feature_poi.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.cesoft.feature_poi.R
import com.cesoft.feature_poi.Util
import com.cesoft.feature_poi.model.Poi
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment() {
    private var poi: Poi? = null
    private var mapZoom = 20f

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        poi = arguments?.get(ARG_PARAM) as Poi
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync { map -> onMapReady(map) }
    }

    private fun onMapReady(map: GoogleMap) {
        map.uiSettings.isZoomControlsEnabled = true
        map.uiSettings.isCompassEnabled = true
        map.uiSettings.isMyLocationButtonEnabled = true
        map.uiSettings.isMapToolbarEnabled = true
        if(Util.checkLocationPermissions(requireActivity())) {
            map.isMyLocationEnabled = true
        }
        map.setOnMapClickListener { latLng: LatLng ->
            //setPosicion(latLng.latitude, latLng.longitude, true)
        }
        poi?.let { poi ->
            val pos = LatLng(poi.latitude, poi.longitude)
            val mo = MarkerOptions()
                .position(pos)
                .title(poi.name)
                .snippet(poi.description)
                .rotation(0f)
            map.addMarker(mo)
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, mapZoom))
        } ?: run {
            val sydney = LatLng(-34.0, 151.0)//TODO: Current location
            map.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
            map.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        }
    }

    companion object {
        private const val TAG = "MapsFragment"
        private val ARG_PARAM = Poi::class.java.simpleName

        private const val DELAY_LOCATION = 30 * 1000//ms
        private const val MAP_ZOOM = "mapzoom"
    }
}