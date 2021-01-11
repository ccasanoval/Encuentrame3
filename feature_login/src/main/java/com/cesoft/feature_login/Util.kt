package com.cesoft.feature_login

import android.content.Context
import android.content.Intent
import android.net.Uri

object Util {
    fun showPrivacyPolicy(context: Context) {
        val browserIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://cesweb-ef91a.firebaseapp.com")
        )
        context.startActivity(browserIntent)
    }
}