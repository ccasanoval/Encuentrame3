package com.cesoft.feature_login

import android.content.Context
import android.content.Intent
import android.util.Log
import com.cesoft.feature_login.model.User
import com.huawei.agconnect.auth.*
import com.huawei.hmf.tasks.TaskExecutors
import com.huawei.hms.common.ApiException
import com.huawei.hms.support.api.entity.auth.Scope
import com.huawei.hms.support.api.entity.common.CommonConstant.SCOPE.ACCOUNT_BASEPROFILE
import com.huawei.hms.support.hwid.HuaweiIdAuthManager
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParams
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParamsHelper
import com.huawei.hms.support.hwid.service.HuaweiIdAuthService
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

data class Result(val user: User?, val exception: Exception?) {
    val isOk: Boolean
        get() = exception == null && user != null
}
class AuthService(context: Context) {
    //: AuthServiceContract
    private val auth: AGConnectAuth = AGConnectAuth.getInstance()
    private val service: HuaweiIdAuthService
    init {
        val paramsHelper = HuaweiIdAuthParamsHelper(HuaweiIdAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
        val scopeList: MutableList<Scope> = ArrayList()
        scopeList.add(Scope(ACCOUNT_BASEPROFILE))
        paramsHelper.setScopeList(scopeList)
        val authParams = paramsHelper.setAccessToken().createParams()
        service = HuaweiIdAuthManager.getService(context, authParams)
    }

    fun isLoggedIn(): Boolean = auth.currentUser != null

    fun logout() = auth.signOut()

    fun getLoginIntent(): Intent = service.signInIntent
    suspend fun login(data: Intent): Result {
        return suspendCoroutine { continuation ->
            val authHuaweiIdTask = HuaweiIdAuthManager.parseAuthResultFromIntent(data)
            if(authHuaweiIdTask.isSuccessful) {
                val huaweiAccount = authHuaweiIdTask.result
                Log.e(TAG, "login-----------accessToken:" + huaweiAccount.accessToken)
                val credential = HwIdAuthProvider.credentialWithToken(huaweiAccount.accessToken)
                auth.signIn(credential)
                    .addOnSuccessListener { signInResult ->
                        Log.e(TAG, "login---------------signInResult:$signInResult")
                        val usr = toModelUser(signInResult.user)
                        continuation.resume(Result(usr, null))
                    }.addOnFailureListener { e ->
                        Log.e(TAG, "login-----------------signInResult:${e.message}")
                        continuation.resume(Result(null, e))
                    }
            }
            else {
                Log.e(TAG,"-------------login(data) failed : " + (authHuaweiIdTask.exception as ApiException))
                continuation.resume(Result(null, authHuaweiIdTask.exception))
            }
        }
    }

    suspend fun login(email: String, pwd: String): Result {
        return suspendCoroutine { continuation ->
            val credential = EmailAuthProvider.credentialWithPassword(email, pwd)
            val usr = AGConnectAuth.getInstance().currentUser
            android.util.Log.e(TAG, "login(email,pwd) error --------------------${credential.provider} $usr")
            if(usr == null)
                continuation.resume(Result(null, Exception("???")))
            else
                continuation.resume(Result(toModelUser(usr), null))
        }
    }

    /*
    *   The password must:
    * Contain at least eight characters.
    * Contain at least two types of the following characters:
    * Lowercase letter
    * Uppercase letter
    * Digit (0–9)
    * Space or special character: `!@#$%^&*()-_=+\|[{}];:'",<.>/?
    * Be different from the mobile number or email address.
    * */
    suspend fun addUser(email: String, pwd: String, verify: String): Result {
        return suspendCoroutine { continuation ->
            val emailUser = EmailUser.Builder()
                .setEmail(email)
                .setPassword(pwd) //optional
                .setVerifyCode(verify)
                .build()
            android.util.Log.e(TAG, "addUser ---------1-----------")
            AGConnectAuth.getInstance().createUser(emailUser)
                .addOnSuccessListener { signInResult ->
                    android.util.Log.e(TAG, "addUser ---------8-----------USER="+signInResult.user)
                    continuation.resume(Result(toModelUser(signInResult.user), null))
                }
                .addOnFailureListener { e ->
                    android.util.Log.e(TAG, "addUser ---------9-----------e=$e")
                    continuation.resume(Result(null, e))
                }
        }
    }
    suspend fun getVerifyCodeEmail(email: String): Boolean {
        return suspendCoroutine { continuation ->
            val settings = VerifyCodeSettings.newBuilder()
                .action(VerifyCodeSettings.ACTION_REGISTER_LOGIN)
                .sendInterval(30) //shortest send interval ，30-120s
                .build()

            android.util.Log.e(TAG, "getVerifyCode:addOnSuccessListener: "+Locale.getDefault())

            val task = EmailAuthProvider.requestVerifyCode(email, settings)
            //task.addOnSuccessListener(TaskExecutors.uiThread(), {
            task
                .addOnSuccessListener(TaskExecutors.immediate(), {
                    android.util.Log.e(TAG, "getVerifyCode:addOnSuccessListener: verify code result = ${it.validityPeriod}")
                    continuation.resume(true)
                })
                .addOnFailureListener(TaskExecutors.immediate(), { e ->
                    android.util.Log.e(TAG, "getVerifyCode:addOnFailureListener: e = $e")
                    continuation.resume(false)
                })
        }
    }
    suspend fun getVerifyCodePhone(phone: String): Boolean {
        return suspendCoroutine { continuation ->

            val settings = VerifyCodeSettings.newBuilder()
                .action(VerifyCodeSettings.ACTION_REGISTER_LOGIN)
                .sendInterval(30) //shortest send interval ，30-120s
                .build()

            val task = PhoneAuthProvider.requestVerifyCode("", phone, settings)//TODO:country code +34
            task
                .addOnSuccessListener(TaskExecutors.immediate(), {
                    android.util.Log.e(TAG, "getVerifyCodePhone:addOnSuccessListener: verify code result = ${it.validityPeriod}")
                    continuation.resume(true)
                })
                .addOnFailureListener(TaskExecutors.immediate(), { e ->
                    android.util.Log.e(TAG, "getVerifyCodePhone:addOnFailureListener: e = $e")
                    continuation.resume(false)
                })
        }
    }

    suspend fun recover(email: String): Boolean {
        return suspendCoroutine { continuation ->
//TODO:-------------------------------------------------------------------------------------
        }
    }

    private fun toModelUser(user: AGConnectUser): User =
        User(
            user.uid,
            user.displayName,
            user.email,
            user.phone,
            user.photoUrl)
    fun getCurrentUser(): User? {
        if(auth.currentUser == null) return null
        return toModelUser(auth.currentUser)
    }

    //TODO: AddUser with phone
    /*fun phone() {
        val countryCode: String = countryCodeEdit.getText().toString().trim { it <= ' ' }
        val phoneNumber: String = accountEdit.getText().toString().trim { it <= ' ' }
        val password: String = passwordEdit.getText().toString().trim { it <= ' ' }
        val verifyCode: String = verifyCodeEdit.getText().toString().trim { it <= ' ' }
        val phoneUser = PhoneUser.Builder()
            .setCountryCode(countryCode)
            .setPhoneNumber(phoneNumber)
            .setPassword(password) //optional
            .setVerifyCode(verifyCode)
            .build()
        AGConnectAuth.getInstance().createUser(phoneUser)
            .addOnSuccessListener {
                startActivity(
                    Intent(
                        this@RegisterActivity,
                        AuthMainActivity::class.java
                    )
                )
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this@RegisterActivity,
                    "createUser fail:$e",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }*/

    companion object {
        private const val TAG = "AuthService:hms"
    }
}
