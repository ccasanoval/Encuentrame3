package com.cesoft.feature_login

import android.content.Intent
import com.cesoft.feature_login.model.User

interface AuthServiceContract {
    fun getCurrentUser(): User?
    fun isLoggedIn(): Boolean
    fun getLoginIntent(): Intent
    suspend fun login(data: Intent): Boolean
    suspend fun login(email: String, pwd: String): Boolean
    fun logout()
    suspend fun addUser(email: String, pwd: String, verify: String=""): Boolean
    suspend fun recover(email: String): Boolean
}