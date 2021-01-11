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
import com.google.android.gms.common.SignInButton
import com.google.android.material.textfield.TextInputLayout
import org.koin.android.ext.android.inject


//NOTE: Remember to change package_name from encuentrame3 to feature_login after downloading google-services.json
// "client": [{
//      "client_info": {
//        "android_client_info": {
//          "package_name": "com.cesoft.feature_login"
//        }
class LoginFragment : Fragment() {
    private val vm: LoginViewModel by inject()
    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val data = result.data
        if(result.resultCode == Activity.RESULT_OK && data != null) {
            vm.login(data)
        }
        else {
            Log.e(tag,"resultLauncher:e: Google Sign In failed: ---------------------- ${result.resultCode} : $data")
            Toast.makeText(context, R.string.login_error_google, Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm.goto.observe(this, { goto: LoginViewModel.GOTO ->
            when(goto) {
                LoginViewModel.GOTO.Finish -> {
                    val act = requireActivity()
                    Log.e(LoginFragment.tag,"LoginViewModel.GOTO.Finish ----------1----" + act.javaClass.canonicalName)
                    Log.e(LoginFragment.tag,"LoginViewModel.GOTO.Finish ----------2----" + vm.getUser())
                    act.finish()
                }
            }
        })
        vm.msg.observe(this, { pair ->
            showWait(false)
            val msg = String.format(getString(pair.first), pair.second)
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
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
        when (sectionNumber) {
            ENTER -> enter(rootView)
            REGISTER -> register(rootView)
            RECOVER -> recover(rootView)
            else -> enter(rootView)
        }
        return rootView
        //return inflater.inflate(R.layout.fragment_login, container, false)
    }

    private fun showWait(on: Boolean) {
        val progressBar: ProgressBar = requireView().findViewById(R.id.progressBar)
        if(on) progressBar.visibility = View.VISIBLE
        else progressBar.visibility = View.GONE
    }

    private fun enter(rootView: View) {
        val lblTitulo = rootView.findViewById<TextView>(R.id.lblTitulo)
        val txtPassword = rootView.findViewById<EditText>(R.id.txtPassword)
        val txtEmail = rootView.findViewById<EditText>(R.id.txtEmail)
        val btnSend = rootView.findViewById<Button>(R.id.btnSend)
        val btnPrivacyPolicy = rootView.findViewById<Button>(R.id.btnPrivacyPolicy)
        val btnGoogle: SignInButton = rootView.findViewById(R.id.btnGoogle)
        val lblPassword2: TextInputLayout = rootView.findViewById(R.id.lblPassword2)
        lblPassword2.visibility = View.GONE
        lblTitulo.text = getString(R.string.enter_lbl)

        /// Show Privacy Policy
        btnPrivacyPolicy.setOnClickListener { v: View? ->
           Util.showPrivacyPolicy(requireContext())
        }

        /// Email account login
        btnSend.setOnClickListener { v: View? ->
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
        btnGoogle.setOnClickListener {
            resultLauncher.launch(vm.getIntent())
        }
    }


    private fun register(rootView: View) {
        val lblTitulo = rootView.findViewById<TextView>(R.id.lblTitulo)
        val txtPassword = rootView.findViewById<EditText>(R.id.txtPassword)
        val txtPassword2 = rootView.findViewById<EditText>(R.id.txtPassword2)
        val txtEmail = rootView.findViewById<EditText>(R.id.txtEmail)
        val btnSend = rootView.findViewById<Button>(R.id.btnSend)
        val btnGoogle = rootView.findViewById<SignInButton>(R.id.btnGoogle)
        val lblLoginGoogle = rootView.findViewById<TextView>(R.id.lblLoginGoogle)
        btnGoogle.visibility = View.GONE
        lblLoginGoogle.visibility = View.GONE
        lblTitulo.text = getString(R.string.register_lbl)
        btnSend.setOnClickListener { v: View? ->
            if(txtEmail.text.isEmpty() || txtPassword.text.isEmpty()) {
                Toast.makeText(context, getString(R.string.register_empty), Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if(txtPassword.text.toString() != txtPassword2.text.toString()) {
                Toast.makeText(context, getString(R.string.register_bad_pass), Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            showWait(true)
            vm.addUser(txtEmail.text.toString(), txtPassword.text.toString())
            /*login.addUser(txtEmail.text.toString(), txtPassword.text.toString(),
                object : AuthListener() {
                    fun onExito(usr: FirebaseUser?) {
                        //main.finEsperaLogin();
                        if (usr != null) Toast.makeText(
                            context,
                            getString(R.string.register_ok) + "  " + usr.email,
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    fun onFallo(e: Exception) {
                        //main.finEsperaLogin();//TODO: Añadir %s en la cadena
                        Toast.makeText(
                            context,
                            getString(R.string.register_ko) + "  " + e,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })*/
        }
    }

    private fun recover(rootView: View) {
        val lblTitulo = rootView.findViewById<TextView>(R.id.lblTitulo)
        val txtEmail = rootView.findViewById<EditText>(R.id.txtEmail)
        val btnSend = rootView.findViewById<Button>(R.id.btnSend)
        val btnGoogle = rootView.findViewById<SignInButton>(R.id.btnGoogle)
        val lblPassword = rootView.findViewById<TextInputLayout>(R.id.lblPassword)
        val lblPassword2 = rootView.findViewById<TextInputLayout>(R.id.lblPassword2)
        val lblLoginGoogle = rootView.findViewById<TextView>(R.id.lblLoginGoogle)
        btnGoogle.visibility = View.GONE
        lblLoginGoogle.visibility = View.GONE
        lblPassword.visibility = View.GONE
        lblPassword2.visibility = View.GONE
        lblTitulo.text = getString(R.string.recover_lbl)
        btnSend.setOnClickListener { v: View? ->
            /*login.restoreUser(txtEmail.text.toString(),
                object : AuthListener() {
                    fun onExito(usr: FirebaseUser?) {
                        Toast.makeText(rootView.context, R.string.recover_ok, Toast.LENGTH_LONG)
                            .show()
                        val activity: ActLogin? = activity as ActLogin?
                        if (activity != null) activity.selectTabEnter()
                    }

                    fun onFallo(e: Exception) {
                        Log.e(tag, String.format("RECOVER:e:%s", e), e)
                        Toast.makeText(
                            rootView.context,
                            R.string.recover_ko.toString() + "  " + e.toString(),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })*/
        }
    }


    companion object {
        private const val tag = "LoginFrg"
        private const val ARG_SECTION_NUMBER = "section_number"

        const val MAX_PAGES = 3
        const val ENTER = 0
        const val REGISTER = 1
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