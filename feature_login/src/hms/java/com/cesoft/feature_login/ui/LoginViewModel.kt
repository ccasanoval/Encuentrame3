package com.cesoft.feature_login.ui

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cesoft.feature_login.AuthService
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class LoginViewModel(private val authService: AuthService): ViewModel() {
    enum class GOTO { Finish }
    private val _goto = MutableLiveData<GOTO>()
    val goto: LiveData<GOTO> = _goto

    fun isLoggedIn() = authService.isLoggedIn()
    fun getIntent() = authService.getLoginIntent()
    fun login(data: Intent) {
        viewModelScope.launch {
            if(authService.login(data)) {
                _goto.postValue(GOTO.Finish)
            }
            else
                android.util.Log.e("LoginVM", "login error --------------------")
        }
    }
}