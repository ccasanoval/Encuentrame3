package com.cesoft.feature_login

import android.content.Context
import android.content.Intent
import com.cesoft.feature_login.model.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AuthService(val context: Context) : AuthServiceContract {

    override fun isLoggedIn(): Boolean = FirebaseAuth.getInstance().currentUser != null

    override fun logout() {
        FirebaseAuth.getInstance().signOut()
    }

    override fun getLoginIntent(): Intent {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        val signInClient = GoogleSignIn.getClient(context, gso)
        return signInClient.signInIntent
    }

    override suspend fun login(data: Intent): Boolean {
        return suspendCoroutine { continuation ->
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.result
            if (task.isSuccessful) {
                account?.let { googleSignInAccount ->
                    val credential = GoogleAuthProvider.getCredential(googleSignInAccount.idToken, null)
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

    override suspend fun login(email: String, pwd: String): Boolean {
        return suspendCoroutine { continuation ->
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, pwd)
                .addOnSuccessListener {
                    continuation.resume(true)
                }
                .addOnFailureListener {
                    continuation.resume(false)
                }
        }
    }

    override fun getCurrentUser(): User {
        val auth = FirebaseAuth.getInstance()
        return User(auth.currentUser?.uid, auth.currentUser?.displayName, auth.currentUser?.email, auth.currentUser?.phoneNumber, auth.currentUser?.photoUrl.toString())
    }

    //TODO: Mostrar reglas de Firebase para crear usuarios...(en caso de error...)
    override suspend fun addUser(email: String, pwd: String, verify: String): Boolean {
        return suspendCoroutine { continuation ->
            val auth = FirebaseAuth.getInstance()
            auth.createUserWithEmailAndPassword(email, pwd)
                .addOnSuccessListener {
                    continuation.resume(true)
                }
                .addOnFailureListener {
                    continuation.resume(false)
                }
        }
    }

    override suspend fun recover(email: String): Boolean {
        return suspendCoroutine { continuation ->
            val auth = FirebaseAuth.getInstance()
            auth.sendPasswordResetEmail(email)
                .addOnSuccessListener {
                    continuation.resume(true)
                }
                .addOnFailureListener {
                    continuation.resume(false)
                }
        }
    }
}