package com.cesoft.feature_login.ui

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cesoft.feature_login.AuthService
import com.cesoft.feature_login.R
import kotlinx.coroutines.launch

class LoginViewModel(private val authService: AuthService): ViewModel() {
    enum class GOTO { Finish }
    private val _goto = MutableLiveData<GOTO>()
    val goto: LiveData<GOTO> = _goto
    private val _msg = MutableLiveData<Pair<Int, String?>>()
    val msg: LiveData<Pair<Int, String?>> = _msg

    fun isLoggedIn() = authService.isLoggedIn()
    fun getIntent() = authService.getLoginIntent()
    fun login(data: Intent) {
        viewModelScope.launch {
            if(authService.login(data)) {
                android.util.Log.e(tag, "login ok --------------------")
                _msg.postValue(Pair(R.string.login_ok, authService.getCurrentUser()?.name))
                _goto.postValue(GOTO.Finish)
            }
            else {
                android.util.Log.e(tag, "login error --------------------")
                _msg.postValue(Pair(R.string.login_error, null))
            }
        }
    }

    companion object {
        private const val tag = "LoginVM"
    }
}