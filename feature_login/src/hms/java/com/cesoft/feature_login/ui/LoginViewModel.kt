package com.cesoft.feature_login.ui

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cesoft.feature_login.AuthService
import com.cesoft.feature_login.R
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class LoginViewModel(private val authService: AuthService): ViewModel() {
    enum class GOTO { Finish }
    private val _goto = MutableLiveData<GOTO>()
    val goto: LiveData<GOTO> = _goto
    private val _msg = MutableLiveData<Pair<Int, String?>>()
    val msg: LiveData<Pair<Int, String?>> = _msg
    private val _clear = MutableLiveData<Boolean>()
    val clear: LiveData<Boolean> = _clear

    fun isLoggedIn() = authService.isLoggedIn()
    fun getIntent() = authService.getLoginIntent()
    fun login(data: Intent) {
        viewModelScope.launch {
            if(authService.login(data)) {
                _goto.postValue(GOTO.Finish)
            }
            else
                android.util.Log.e(tag, "login error --------------------")
        }
    }

    fun login(email: String, pwd: String) {
        viewModelScope.launch {
            if(authService.login(email, pwd)) {
                android.util.Log.e(tag, "login email & pass ok --------------------")
                _msg.postValue(Pair(R.string.login_ok, authService.getCurrentUser()?.secureName))
                _goto.postValue(GOTO.Finish)
            }
            else {
                android.util.Log.e(tag, "login error --------------------")
                _msg.postValue(Pair(R.string.login_error, null))
            }
        }
    }

    fun addUser(email: String, pwd: String, verify: String) {
        viewModelScope.launch {
            if(authService.addUser(email, pwd, verify)) {
                android.util.Log.e(tag, "addUser ok --------------------")
                _msg.postValue(Pair(R.string.signin_ok, authService.getCurrentUser()?.email))
                _clear.postValue(true)
            }
            else {
                android.util.Log.e(tag, "addUser error --------------------")
                _msg.postValue(Pair(R.string.signin_error, null))
            }
        }
    }
    fun getVerifyCodeEmail(email: String) {
        viewModelScope.launch {
            if(authService.getVerifyCodeEmail(email)) {
                _msg.postValue(Pair(R.string.signin_verify_code_sent_email, null))
            }
            else {
                _msg.postValue(Pair(R.string.signin_verify_code_not_sent, null))
            }
        }
    }
    fun getVerifyCodePhone(phone: String) {
        viewModelScope.launch {
            if(authService.getVerifyCodePhone(phone)) {
                _msg.postValue(Pair(R.string.signin_verify_code_sent_phone, null))
            }
            else {
                _msg.postValue(Pair(R.string.signin_verify_code_not_sent, null))
            }
        }
    }

    fun recover(email: String) {
        viewModelScope.launch {
            if(authService.recover(email)) {
                android.util.Log.e(tag, "recover ok --------------------")
                _msg.postValue(Pair(R.string.recover_ok, authService.getCurrentUser()?.email))
            }
            else {
                android.util.Log.e(tag, "recover error --------------------")
                _msg.postValue(Pair(R.string.recover_error, null))
            }
        }
    }

    companion object {
        private const val tag = "LoginVM"
    }
}