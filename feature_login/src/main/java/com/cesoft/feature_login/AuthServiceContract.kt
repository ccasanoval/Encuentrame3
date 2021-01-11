package com.cesoft.feature_login

import android.content.Intent
import com.cesoft.feature_login.model.User

interface AuthServiceContract {
    fun isLoggedIn(): Boolean
    //suspend fun login():  Boolean
    fun logout()
    fun getLoginIntent(): Intent
    suspend fun login(data: Intent): Boolean

    fun getCurrentUser(): User?
//    fun getCurrentUserName() : String?
//    fun getCurrentUserEmail(): String?
//    fun getCurrentUserImage(): String?
//    fun getCurrentUserPhone(): String?
//    fun getCurrentUserId(): String?
}