package com.cesoft.feature_poi.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cesoft.feature_poi.R
import com.cesoft.feature_poi.model.Poi
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*


class PoiFragment : Fragment() {
    private lateinit var vm: PoiViewModel

    //private var vozMenuItem: MenuItem? = null
    private lateinit var txtName: EditText
    private lateinit var txtDescription: EditText
    private lateinit var lblPosicion: TextView
    //private lateinit var btnActPos: ImageButton
    private lateinit var progressBar: ProgressBar
    private lateinit var map: GoogleMap
    private var mapZoom = 20f


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_poi, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm = ViewModelProvider(this).get(PoiViewModel::class.java)
        vm.poi = arguments?.get(ARG_PARAM) as Poi
        Log.e(TAG, "------------poi=${vm.poi}")

        txtName = view.findViewById(R.id.txtName)
        txtDescription = view.findViewById(R.id.txtDescription)
        lblPosicion = view.findViewById(R.id.lblPosicion)
        progressBar = view.findViewById(R.id.progressBar)
        vm.poi?.let { poi ->
            txtName.setText(poi.name)
            txtDescription.setText(poi.description)
            setPosLabel(poi.latitude, poi.longitude)
        }

        val btnActPos = view.findViewById<ImageButton>(R.id.btnActPos)
        btnActPos.setOnClickListener {

        }

        val fabBuscar = view.findViewById<FloatingActionButton>(R.id.fabSearch)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync { map -> onMapReady(map) }
    }
    private fun onMapReady(map: GoogleMap) {
        this.map = map
        map.uiSettings.isZoomControlsEnabled = true
        map.uiSettings.isCompassEnabled = true
        map.uiSettings.isMyLocationButtonEnabled = true
        map.uiSettings.isMapToolbarEnabled = true
        map.setOnMapClickListener { latLng: LatLng ->
            //setPosicion(latLng.latitude, latLng.longitude, true)
        }
        vm.poi?.let { poi ->
            val pos = LatLng(poi.latitude, poi.longitude)
            val mo = MarkerOptions()
                .position(pos)
                .title(poi.name)
                .rotation(0f)
            map.addMarker(mo)
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, mapZoom))
        } ?: run {
            val sydney = LatLng(-34.0, 151.0)//TODO: Current location
            map.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
            map.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                android.util.Log.e(TAG, "onOptionsItemSelected HOME ------------------------------")
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
/*
    // Implements OnMapReadyCallback
    fun onMapReady(googleMap: GoogleMap?) {
        Log.e(TAG, "onMapReady------------")

        map = googleMap
        map?.uiSettings?.isZoomControlsEnabled = true
        try {
            map?.isMyLocationEnabled = true
        } catch (ignored: SecurityException) { }
        //https://developers.google.com/maps/documentation/android-api/map?hl=es-419
        //map.setMapType(GoogleMap.MAP_TYPE_NORMAL y GoogleMap.MAP_TYPE_SATELLITE
        map?.setOnCameraIdleListener {
            map?.let {
                mapZoom = it.cameraPosition.zoom
            }
        }
//        if (presenterBase.getLatitud() === 0 && presenterBase.getLongitud() === 0 && presenterBase.isNuevo()) {
//            val loc: Location = util.getLocation()
//            if (loc != null) presenterBase.setLatLon(loc.latitude, loc.longitude)
//        }
//        setPosLugar(presenterBase.getLatitud(), presenterBase.getLongitud())
        map!!.animateCamera(CameraUpdateFactory.zoomTo(mapZoom))
    }*/
    private fun setPosLabel(lat: Double, lon: Double) {
    lblPosicion.text = String.format(Locale.ENGLISH, "%.5f/%.5f", lat, lon)
    }



    companion object {
        private const val TAG = "PoiFragment"
        private val ARG_PARAM = Poi::class.java.simpleName

        private const val DELAY_LOCATION = 30 * 1000//ms
        private const val MAP_ZOOM = "mapzoom"

        @JvmStatic fun newInstance(param: String) =
            PoiFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM, param)
                }
            }
    }
}