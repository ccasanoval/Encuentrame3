package com.cesoft.feature_login.ui

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import com.cesoft.feature_login.R
import org.koin.android.ext.android.inject

class LoginActivity : AppCompatActivity() {
    private val vm: LoginViewModel by inject()
    // New api fails: java.lang.IllegalArgumentException: Can only use lower 16 bits for requestCode
    private var resultLauncher = registerForActivityResult(StartActivityForResult()) { result ->
        val data = result.data
        if(result.resultCode == Activity.RESULT_OK && data != null) {
            vm.login(data)
            Toast.makeText(this, "Login success: $data", Toast.LENGTH_SHORT).show()
            android.util.Log.e("LoginAct", "resultLauncher-----Ok--------- $data --------------------")
        }
        else {
            android.util.Log.e("LoginAct", "resultLauncher-----Error--------- $data --------------------")
            Toast.makeText(this, "Login error: $data", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        android.util.Log.e(tag, "onCreate------------------------")
        vm.goto.observe(this, { goto ->
            when(goto) {
                LoginViewModel.GOTO.Finish -> {
                    finish()
                }
            }
        })

        if(vm.isLoggedIn()) {
            android.util.Log.e(tag, "Already logged in...")
            finish()
            return
        }

        val btnHuaweiId = findViewById<Button>(R.id.btnHuaweiId)
        btnHuaweiId.setOnClickListener {
            login()
        }

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
    }

    fun login() {
        android.util.Log.e(tag, "login----------------------------------")
        resultLauncher.launch(vm.getIntent())
    }

    companion object {
        private const val tag = "LoginAct"
    }
}