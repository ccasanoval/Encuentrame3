package com.cesoft.encuentrame3.ui

import android.view.MenuItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cesoft.encuentrame3.R
import com.cesoft.feature_login.AuthService
import com.google.android.material.navigation.NavigationView
import kotlin.system.exitProcess

class MainViewModel(private val authService: AuthService): ViewModel(),
    NavigationView.OnNavigationItemSelectedListener {

    enum class GOTO { Login, PrivacyPolicy, Settings, Exit }
    private val _goto = MutableLiveData<GOTO>()
    val goto: LiveData<GOTO> = _goto

    fun isLoggedIn() = authService.isLoggedIn()
    fun getCurrentUser() = authService.getCurrentUser()

//    private fun logout() {
//        viewModelScope.launch {
//            authService.logout()
//        }
//    }

    //Implements NavigationView.OnNavigationItemSelectedListener
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.nav_geofencing_onoff -> {
                //if(GeofencingService.isOn()) GeofencingService.stop() else GeofencingService.start()
                true
            }
            R.id.nav_config, R.id.nav_voice, R.id.nav_about -> {
                _goto.postValue(GOTO.Settings)
                true
            }
            R.id.nav_privacy_policy -> {
                _goto.postValue(GOTO.PrivacyPolicy)
                true
            }
            R.id.nav_logout -> {
                authService.logout()
                _goto.postValue(GOTO.Login)
                true
            }
            R.id.nav_exit -> {
                //GeofencingService.stop()
                _goto.postValue(GOTO.Exit)
                exitProcess(0)
            }
            else -> false
        }
    }

    companion object {
        private const val TAG = "MainVM"
    }
}