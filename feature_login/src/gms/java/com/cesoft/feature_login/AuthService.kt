package com.cesoft.feature_login

class AuthService : AuthServiceContract {

    override fun isLoggedIn(): Boolean = false
    override fun login() {  }
    override fun logout() {  }
    override fun getLoginIntent(): Intent = Intent()
    override fun login(data: Intent) {

    }

    override fun getCurrentUser(): User? {
        return User(
            getCurrentUserId(),
            getCurrentUserName(),
            getCurrentUserEmail(),
            getCurrentUserPhone(),
            getCurrentUserImage()
        )
    }
    override fun getCurrentUserId(): String? = ""
    override fun getCurrentUserName() : String? = ""
    override fun getCurrentUserEmail(): String? = ""
    override fun getCurrentUserPhone(): String? = ""
    override fun getCurrentUserImage(): String? = ""
}