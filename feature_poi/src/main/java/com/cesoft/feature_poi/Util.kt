package com.cesoft.feature_poi

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

object Util {
    fun checkLocationPermissions(activiy: Activity): Boolean {
        return if(checkPermission(activiy, Manifest.permission.ACCESS_FINE_LOCATION) || checkPermission(activiy, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            true
        } else {
            activiy.requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 999)
            false
        }
    }
    private fun checkPermission(context: Context, permission: String): Boolean =
        ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
}