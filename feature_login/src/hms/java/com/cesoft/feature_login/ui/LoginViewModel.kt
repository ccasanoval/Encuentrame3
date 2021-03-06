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
    private val _clear = MutableLiveData<Boolean>()
    val clear: LiveData<Boolean> = _clear

    fun isLoggedIn() = authService.isLoggedIn()
    fun getIntent() = authService.getLoginIntent()
    fun login(data: Intent) {
        viewModelScope.launch {
            val res = authService.login(data)
            if(res.isOk) {
                android.util.Log.e(TAG, "login(data) usr --------------------"+res.user)
                _goto.postValue(GOTO.Finish)
            }
            else
                android.util.Log.e(TAG, "login(data) error --------------------("+res.exception+")")
        }
    }

    fun login(email: String, pwd: String) {
        viewModelScope.launch {
            val res = authService.login(email, pwd)
            if(res.isOk) {
                android.util.Log.e(TAG, "login email & pass ok --------------------"+res.user)
                _msg.postValue(Pair(R.string.login_ok,res.user?.secureName))
                _goto.postValue(GOTO.Finish)
            }
            else {
                android.util.Log.e(TAG, "login(email,pwd) error --------------------"+res.exception)
                _msg.postValue(Pair(R.string.login_error, null))
            }
        }
    }

    fun addUser(email: String, pwd: String, verify: String) {
        viewModelScope.launch {
            val res = authService.addUser(email, pwd, verify)
            if(res.isOk) {
                android.util.Log.e(TAG, "addUser ok --------------------"+res.user)
                _msg.postValue(Pair(R.string.signin_ok, res.user?.email))
                _clear.postValue(true)
            }
            else {
                android.util.Log.e(TAG, "addUser error --------------------"+res.exception)
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
                android.util.Log.e(TAG, "recover ok --------------------")
                _msg.postValue(Pair(R.string.recover_ok, authService.getCurrentUser()?.email))
            }
            else {
                android.util.Log.e(TAG, "recover error --------------------")
                _msg.postValue(Pair(R.string.recover_error, null))
            }
        }
    }

    companion object {
        private const val TAG = "LoginVM"
    }
}