package com.cesoft.feature_login

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.cesoft.feature_login.model.User
import com.cesoft.feature_login.ui.LoginViewModel
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

class AuthService(context: Context) : AuthServiceContract {
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

    override fun isLoggedIn(): Boolean = auth.currentUser != null

    override fun logout() = auth.signOut()

    override fun getLoginIntent(): Intent = service.signInIntent
    override suspend fun login(data: Intent): Boolean {
        return suspendCoroutine { continuation ->
            val authHuaweiIdTask = HuaweiIdAuthManager.parseAuthResultFromIntent(data)
            if(authHuaweiIdTask.isSuccessful) {
                val huaweiAccount = authHuaweiIdTask.result
                Log.i(tag, "accessToken:" + huaweiAccount.accessToken)
                val credential = HwIdAuthProvider.credentialWithToken(huaweiAccount.accessToken)
                auth.signIn(credential)
                    .addOnSuccessListener { signInResult ->
                        Log.i(tag, "signInResult:$signInResult")
                        continuation.resume(true)
                    }.addOnFailureListener { e ->
                        Log.i(tag, "signInResult:${e.message}")
                        continuation.resume(false)
                    }
            }
            else {
                Log.e(
                    tag,
                    "sign in failed : " + (authHuaweiIdTask.exception as ApiException).statusCode
                )
                continuation.resume(false)
            }
        }
    }

    override suspend fun login(email: String, pwd: String): Boolean {
        return suspendCoroutine { continuation ->
            val credential = EmailAuthProvider.credentialWithPassword(email, pwd)
            val usr = AGConnectAuth.getInstance().currentUser
            continuation.resume(usr != null)
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
    override suspend fun addUser(email: String, pwd: String, verify: String): Boolean {
        return suspendCoroutine { continuation ->
            val emailUser = EmailUser.Builder()
                .setEmail(email)
                .setPassword(pwd) //optional
                .setVerifyCode(verify)
                .build()
            android.util.Log.e(tag, "addUser ---------1-----------")
            AGConnectAuth.getInstance().createUser(emailUser)
                .addOnSuccessListener {
                    android.util.Log.e(tag, "addUser ---------8-----------e=")
                    continuation.resume(true)
                }
                .addOnFailureListener { e ->
                    android.util.Log.e(tag, "addUser ---------9-----------e=$e")
                    continuation.resume(false)
                }
        }
    }
    suspend fun getVerifyCodeEmail(email: String): Boolean {
        return suspendCoroutine { continuation ->
            val settings = VerifyCodeSettings.newBuilder()
                .action(VerifyCodeSettings.ACTION_REGISTER_LOGIN)
                .sendInterval(30) //shortest send interval ，30-120s
                .build()

            android.util.Log.e(tag, "getVerifyCode:addOnSuccessListener: "+Locale.getDefault())

            val task = EmailAuthProvider.requestVerifyCode(email, settings)
            //task.addOnSuccessListener(TaskExecutors.uiThread(), {
            task
                .addOnSuccessListener(TaskExecutors.immediate(), {
                    android.util.Log.e(tag, "getVerifyCode:addOnSuccessListener: verify code result = ${it.validityPeriod}")
                    continuation.resume(true)
                })
                .addOnFailureListener(TaskExecutors.immediate(), { e ->
                    android.util.Log.e(tag, "getVerifyCode:addOnFailureListener: e = $e")
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
                    android.util.Log.e(tag, "getVerifyCodePhone:addOnSuccessListener: verify code result = ${it.validityPeriod}")
                    continuation.resume(true)
                })
                .addOnFailureListener(TaskExecutors.immediate(), { e ->
                    android.util.Log.e(tag, "getVerifyCodePhone:addOnFailureListener: e = $e")
                    continuation.resume(false)
                })
        }
    }

    override suspend fun recover(email: String): Boolean {
        return suspendCoroutine { continuation ->
//TODO:-------------------------------------------------------------------------------------
        }
    }

    override fun getCurrentUser(): User? {
        if(auth.currentUser == null) return null
        return User(
            auth.currentUser.uid,
            auth.currentUser.displayName,
            auth.currentUser.email,
            auth.currentUser.phone,
            auth.currentUser.photoUrl
        )
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
        private const val tag = "AuthService:hms"
    }
}
