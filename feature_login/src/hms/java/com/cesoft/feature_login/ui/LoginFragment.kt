package com.cesoft.feature_login.ui

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.cesoft.feature_login.R
import com.cesoft.feature_login.Util
import org.koin.android.ext.android.inject
import java.util.regex.Matcher
import java.util.regex.Pattern


class LoginFragment : Fragment() {
    private val vm: LoginViewModel by inject()
    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val data = result.data
        if(result.resultCode == Activity.RESULT_OK && data != null) {
            vm.login(data)
        }
        else {
            Log.e(tag,"resultLauncher:e: Huawei Sign In failed: ---------------------- ${result.resultCode} : $data")
            Toast.makeText(context, R.string.login_error_google, Toast.LENGTH_LONG).show()
        }
    }
    private lateinit var lblTitulo: TextView
    private lateinit var lblLoginServiceId: TextView
    private lateinit var txtEmail: EditText
    private lateinit var txtPassword: EditText
    private lateinit var txtPassword2: EditText
    private lateinit var txtVerifyCode: EditText
    private lateinit var txtPhone: EditText
    private lateinit var btnSend: Button
    private lateinit var btnGetVerifyCodeEmail: Button
    private lateinit var btnGetVerifyCodePhone: Button
    private lateinit var btnLoginServiceId: Button
    private lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm.goto.observe(this, { goto: LoginViewModel.GOTO ->
            when (goto) {
                LoginViewModel.GOTO.Finish -> {
                    val act = requireActivity()
                    act.finish()
                }
            }
        })
        vm.msg.observe(this, { pair ->
            showWait(false)
            val msg = String.format(getString(pair.first), pair.second)
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
        })
        vm.clear.observe(this, {
            clearFields()
        })

        if(vm.isLoggedIn()) {
            requireActivity().finish()
            return
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val sectionNumber = arguments?.getInt(ARG_SECTION_NUMBER) ?: 0
        val rootView: View = inflater.inflate(R.layout.fragment_login, container, false)
        iniFields(rootView)
        when(sectionNumber) {
            ENTER -> login()
            SIGNIN -> signIn()
            RECOVER -> recover()
            else -> login()
        }
        return rootView
    }
    private fun iniFields(rootView: View) {
        lblTitulo = rootView.findViewById(R.id.lblTitulo)
        lblLoginServiceId = rootView.findViewById(R.id.lblLoginServiceId)
        txtEmail = rootView.findViewById(R.id.txtEmail)
        txtPassword = rootView.findViewById(R.id.txtPassword)
        txtPassword2 = rootView.findViewById(R.id.txtPassword2)
        txtVerifyCode = rootView.findViewById(R.id.txtVerifyCode)
        txtPhone = rootView.findViewById(R.id.txtPhone)
        btnSend = rootView.findViewById(R.id.btnSend)
        btnGetVerifyCodeEmail = rootView.findViewById(R.id.btnGetVerifyCodeEmail)
        btnGetVerifyCodePhone = rootView.findViewById(R.id.btnGetVerifyCodePhone)
        btnLoginServiceId = rootView.findViewById(R.id.btnLoginServiceId)
        progressBar = rootView.findViewById(R.id.progressBar)
        val btnPrivacyPolicy = rootView.findViewById<Button>(R.id.btnPrivacyPolicy)
        btnPrivacyPolicy.setOnClickListener {
            Util.showPrivacyPolicy(requireContext())
        }
    }
    private fun clearFields() {
        txtEmail.text.clear()
        txtPassword.text.clear()
        txtPassword2.text.clear()
        txtPhone.text.clear()
    }

    private fun showWait(on: Boolean) {
        //val progressBar: ProgressBar = requireView().findViewById(R.id.progressBar)
        if(on) progressBar.visibility = View.VISIBLE
        else progressBar.visibility = View.GONE
    }

    /// Login
    private fun login() {
        txtPassword2.visibility = View.GONE
        txtVerifyCode.visibility = View.GONE
        txtPhone.visibility = View.GONE
        btnGetVerifyCodeEmail.visibility = View.GONE
        btnGetVerifyCodePhone.visibility = View.GONE
        lblTitulo.text = getString(R.string.enter_lbl)

        /// Email account login
        btnSend.setOnClickListener {
            val email = txtEmail.text.toString()
            val pwd = txtPassword.text.toString()
            if(email.isEmpty() || pwd.isEmpty()) {
                Toast.makeText(requireContext(), R.string.login_error, Toast.LENGTH_LONG).show()
            }
            else {
                showWait(true)
                vm.login(email, pwd)
            }
        }
        txtPassword.setOnEditorActionListener { v: TextView?, actionId: Int, event: KeyEvent? ->
            if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER
                || actionId == EditorInfo.IME_ACTION_DONE
            ) {
                btnSend.callOnClick()
            }
            false
        }

        /// Google account login
        btnLoginServiceId.setOnClickListener {
            resultLauncher.launch(vm.getIntent())
        }
    }

    /// Sign In
    private fun signIn() {
        btnLoginServiceId.visibility = View.GONE
        lblLoginServiceId.visibility = View.GONE
        lblTitulo.text = getString(R.string.signin_lbl)
        btnSend.setText(R.string.signin_btn)
        btnSend.setOnClickListener {
            if(validatePwd(txtEmail.text.toString(), txtPassword.text.toString(), txtPassword2.text.toString())) {
                showWait(true)
                vm.addUser(txtEmail.text.toString(), txtPassword.text.toString(), txtVerifyCode.text.toString())
            }
        }
        btnGetVerifyCodeEmail.setOnClickListener {
            if(txtEmail.text.isEmpty()) {
                Toast.makeText(requireContext(), R.string.signin_email_empty, Toast.LENGTH_LONG).show()
            }
            else {
                vm.getVerifyCodeEmail(txtEmail.text.toString())
            }
        }
        btnGetVerifyCodePhone.setOnClickListener {
            if(txtPhone.text.isEmpty()) {
                Toast.makeText(requireContext(), R.string.signin_phone_empty, Toast.LENGTH_LONG).show()
            }
            else {
                vm.getVerifyCodePhone(txtPhone.text.toString())
            }
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
    * REGEX = (?=(?:.*?[0-9]){2})
    * */
    private fun validatePwd(email: String, pwd: String, pwd2: String): Boolean {
        if(email.isEmpty() || pwd.isEmpty()) {
            Toast.makeText(context, getString(R.string.signin_empty), Toast.LENGTH_LONG).show()
            return false
        }
        if(email == pwd) {
            Toast.makeText(requireContext(), R.string.signin_pwd_email_error, Toast.LENGTH_LONG).show()
            return false
        }
        if(pwd != pwd2) {
            Toast.makeText(requireContext(), R.string.signin_bad_pass, Toast.LENGTH_LONG).show()
            return false
        }
        if(pwd.length < 8) {
            Toast.makeText(requireContext(), R.string.signin_pwd_8min_error, Toast.LENGTH_LONG).show()
            return false
        }

        val pattern: Pattern
        val matcher: Matcher
        val patternStr = "^(?=.{8,}$)(?=(?:.*?[A-Z]){2})(?=(?:.*?[a-z]){2})(?=(?:.*?[0-9]){2})(?=(?:.*?[`!@#$%^&*()\\-_=+|\\[{}\\];:'\",<.>/?]){2}).*$"
        //val patternStr = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[`!@#\$%^&*()\\-_=+|\\[{}\\];:'\",<.>/?])(?=\\S+$).{4,}$"
        pattern = Pattern.compile(patternStr)
        matcher = pattern.matcher(pwd)
        if( ! matcher.matches()) {
            Toast.makeText(requireContext(), R.string.signin_pwd_8min_error, Toast.LENGTH_LONG).show()
        }

        return true
    }

    /// Recover
    private fun recover() {
        txtVerifyCode.visibility = View.GONE
        txtPhone.visibility = View.GONE
        btnGetVerifyCodeEmail.visibility = View.GONE
        btnGetVerifyCodePhone.visibility = View.GONE
        btnLoginServiceId.visibility = View.GONE
        lblLoginServiceId.visibility = View.GONE
        txtPassword.visibility = View.GONE
        txtPassword2.visibility = View.GONE
        lblTitulo.text = getString(R.string.recover_lbl)
        btnSend.setText(R.string.recover_btn)
        btnSend.setOnClickListener {
            if(txtEmail.text.isEmpty()) {
                Toast.makeText(context, getString(R.string.recover_empty), Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            showWait(true)
            vm.recover(txtEmail.text.toString())
        }
    }


    companion object {
        //private const val tag = "LoginFrg"
        private const val ARG_SECTION_NUMBER = "section_number"

        const val MAX_PAGES = 3
        const val ENTER = 0
        const val SIGNIN = 1
        const val RECOVER = 2

        @JvmStatic
        fun newInstance(sectionNumber: Int): LoginFragment {
            val fragment = LoginFragment()
            val args = Bundle()
            args.putInt(ARG_SECTION_NUMBER, sectionNumber)
            fragment.arguments = args
            return fragment
        }
    }

}