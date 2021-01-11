package com.cesoft.feature_login

import android.content.Context
import android.content.Intent
import android.util.Log
import com.cesoft.feature_login.model.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AuthService(val context: Context) : AuthServiceContract {
    override fun isLoggedIn(): Boolean = FirebaseAuth.getInstance().currentUser != null
    //override fun login() {  }
    override fun logout() {
        FirebaseAuth.getInstance().signOut()
    }
    override fun getLoginIntent(): Intent {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        val signInClient: GoogleSignInClient = GoogleSignIn.getClient(context, gso)
        return signInClient.signInIntent
    }
    override suspend fun login(data: Intent): Boolean {
        return suspendCoroutine { continuation ->
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.result
            if (task.isSuccessful) {
                account?.let { googleSignInAccount ->
                    val credential = GoogleAuthProvider.getCredential(
                        googleSignInAccount.idToken,
                        null
                    )
                    val firebaseAuth = FirebaseAuth.getInstance()
                    firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task: Task<AuthResult?> ->
                        if(task.exception == null)
                            continuation.resume(true)
                        else
                            continuation.resume(false)
                    }
                }
            }
        }
    }



    override fun getCurrentUser(): User? {
        val auth = FirebaseAuth.getInstance()
        return User(
            auth.currentUser?.uid,
            auth.currentUser?.displayName,
            auth.currentUser?.email,
            auth.currentUser?.phoneNumber,
            auth.currentUser?.photoUrl.toString()
        )
    }
//    override fun getCurrentUserId(): String? = user?.uid
//    override fun getCurrentUserName() : String? = user?.name
//    override fun getCurrentUserEmail(): String? = user?.email
//    override fun getCurrentUserPhone(): String? = user?.phone
//    override fun getCurrentUserImage(): String? = user?.image
}