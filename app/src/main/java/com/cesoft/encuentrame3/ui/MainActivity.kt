package com.cesoft.encuentrame3.ui

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.cesoft.encuentrame3.R
import com.cesoft.feature_login.ui.LoginActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawerLayout: DrawerLayout
    private val vm: MainViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupToolbar()
        setupDrawerMenu()
        setupFloatingButton()
        //setupTabs()
        iniLivedata()
        checkLogin()
    }
    private fun checkLogin(): Boolean {
        val isLoggedIn = vm.isLoggedIn()
        android.util.Log.e(TAG, "-----------------checkLogin = $isLoggedIn user = " + vm.getCurrentUser())
        if(!isLoggedIn) {
            goLogin()
        }
        return isLoggedIn
    }
    private fun goLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
    private fun setupDrawerMenu() {
        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.nav_main, R.id.nav_poi_list, R.id.nav_poi_item),
            drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navView.setNavigationItemSelectedListener(vm)
    }
    private fun iniCurrentUserHeader() {
        val user = vm.getCurrentUser()
        val view: NavigationView = findViewById(R.id.nav_view)
        val userName = view.getHeaderView(0).findViewById<TextView>(R.id.userName)
        val userEmail= view.getHeaderView(0).findViewById<TextView>(R.id.userEmail)
        val userImage= view.getHeaderView(0).findViewById<ImageView>(R.id.userImage)
        userName?.text = user.secureName
        userEmail?.text = user.email
        if( ! user.image.isNullOrBlank()) {
            val listener = object: CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    userImage.setImageBitmap(resource)
                }
                override fun onLoadCleared(placeholder: Drawable?) { }
            }
            Glide.with(this)
                .asBitmap()
                .load(user.image)
                .into(listener)
        }
    }
    private fun setupToolbar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
    }
    private fun setupFloatingButton() {
//        val fab: FloatingActionButton = findViewById(R.id.fab)
//        fab.setOnClickListener { view ->
//            Snackbar
//                .make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction(  "Action",  null)
//                .show()
//        }
    }

    private fun iniLivedata() {
        vm.goto.observe(this, { goto: MainViewModel.GOTO ->
            drawerLayout.closeDrawers()
            when (goto) {
                MainViewModel.GOTO.Login -> {
                    goLogin()
                }
                MainViewModel.GOTO.Exit -> {
                    finish()
                }
                MainViewModel.GOTO.PrivacyPolicy -> {
                    intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://cesweb-ef91a.firebaseapp.com")
                    )
                    startActivity(intent)
                }
                MainViewModel.GOTO.Settings -> {
                    //intent = Intent(this, ActSettings::class.java)
                    //intent.putExtra(Constantes.SETTINGS_PAGE, id)
                    //startActivity(intent)
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        iniCurrentUserHeader()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    companion object {
        private const val TAG = "MainAct"
    }
}