package com.cesoft.feature_login.model

data class User(
    val uid: String?,
    val name: String?,
    val email: String?,
    val phone: String?,
    val image: String?
) {
    val secureName: String
        get() {
            return if(name.isNullOrEmpty()) {
                if(email.isNullOrEmpty()) {
                    if(phone.isNullOrEmpty()) "?"
                    else phone
                } else {
                    val i = email.indexOf('@')
                    if( i <= 0) email
                    else email.substring(0, i)
                }
            }
            else name
        }
}