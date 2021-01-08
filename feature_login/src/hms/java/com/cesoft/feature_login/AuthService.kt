package com.cesoft.feature_login

import android.content.Context
import android.content.Intent
import android.util.Log
import com.cesoft.feature_login.model.User
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.agconnect.auth.HwIdAuthProvider
import com.huawei.hms.common.ApiException
import com.huawei.hms.support.api.entity.auth.Scope
import com.huawei.hms.support.api.entity.common.CommonConstant.SCOPE.ACCOUNT_BASEPROFILE
import com.huawei.hms.support.hwid.HuaweiIdAuthManager
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParams
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParamsHelper
import com.huawei.hms.support.hwid.service.HuaweiIdAuthService
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
//    override suspend fun login(): Boolean {
//        return suspendCoroutine { continuation ->
//            val task = service.silentSignIn()
//            task.addOnCompleteListener { t ->
//                android.util.Log.e(tag, "login: isCanceled=${t.isCanceled} isComplete=${t.isComplete} isSuccessful=${t.isSuccessful}")
//                continuation.resume(t.isSuccessful)
//            }
//        }
//    }
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
                Log.e(tag, "sign in failed : " + (authHuaweiIdTask.exception as ApiException).statusCode)
                continuation.resume(false)
            }
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
    override fun getCurrentUserName() : String? = auth.currentUser?.displayName
    override fun getCurrentUserEmail(): String? = auth.currentUser?.email
    override fun getCurrentUserImage(): String? = auth.currentUser?.photoUrl
    override fun getCurrentUserPhone(): String? = auth.currentUser?.phone
    override fun getCurrentUserId(): String? = auth.currentUser?.uid

    companion object {
        private const val tag = "AuthService:hms"
    }
}
