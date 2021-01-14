package com.cesoft.feature_poi.ui

import androidx.lifecycle.ViewModel
import com.cesoft.feature_poi.model.Poi
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest

class PoiViewModel : ViewModel() {
    var poi: Poi? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var locationRequest: LocationRequest? = null
    private var idLayout = 0

    /* TODO: meter en utiles y llamar al inicio para comprobar que tiene servicios...
	protected boolean checkPlayServices() {
		GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
		int result = googleAPI.isGooglePlayServicesAvailable(this);
		if (result != ConnectionResult.SUCCESS) {
			Log.e(TAG, "checkPlayServices:e:--------------------------------------------------" + result);
			return false;
		}
		return true;
	}*/
    fun buildLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest!!.interval = DELAY_LOCATION
        locationRequest!!.fastestInterval = DELAY_LOCATION
        locationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    fun clean() {
        //stopTracking()
        locationRequest = null
//        if (map != null) {
//            map.clear()
//            map = null
//        }
    }


    companion object {
        private const val TAG = "PoiVM"
        private const val DELAY_LOCATION = 30 * 1000L//ms
        private const val MAP_ZOOM = "mapzoom"
    }
}