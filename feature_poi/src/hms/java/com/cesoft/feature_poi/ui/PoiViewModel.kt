package com.cesoft.feature_poi.ui

import androidx.lifecycle.ViewModel
import com.cesoft.feature_poi.model.Poi


class PoiViewModel : ViewModel() {
    var poi: Poi? = null

    private var idLayout = 0

    fun clean() {

    }


    companion object {
        private const val TAG = "PoiVM"
        private const val DELAY_LOCATION = 30 * 1000L//ms
        private const val MAP_ZOOM = "mapzoom"
    }
}