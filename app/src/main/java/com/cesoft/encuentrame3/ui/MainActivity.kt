package com.cesoft.encuentrame3.ui

import android.content.Intent
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
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.cesoft.encuentrame3.R
import com.cesoft.feature_login.ui.LoginActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawerLayout: DrawerLayout
    private val vm: MainViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(checkLogin()) {
            setContentView(R.layout.activity_main)
            setupToolbar()
            setupDrawerMenu()
            setupFloatingButton()
            setupTabs()
            iniLivedata()
        }
    }
    private fun checkLogin(): Boolean {
        val isLoggedIn = vm.isLoggedIn()
        android.util.Log.e("MainAct", "checkLogin = $isLoggedIn user = " + vm.getCurrentUser())
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
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(R.id.nav_feature_poi/*, R.id.nav_gallery, R.id.nav_slideshow*/), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        iniCurrentUserHeader()
        navView.setNavigationItemSelectedListener(vm)
    }
    private fun iniCurrentUserHeader() {
        val user = vm.getCurrentUser()
        val view: NavigationView = findViewById(R.id.nav_view)
        val userName = view.getHeaderView(0).findViewById<TextView>(R.id.userName)
        val userEmail= view.getHeaderView(0).findViewById<TextView>(R.id.userEmail)
        val userImage= view.getHeaderView(0).findViewById<ImageView>(R.id.userImage)
        userName?.text = user?.name
        userEmail?.text = user?.email
        if(userImage != null && user?.image != null)
        Glide.with(this)
            .asBitmap()
            .load(user.image)
            .into(userImage)
    }
    private fun setupToolbar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
    }
    private fun setupFloatingButton() {
        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show()
        }
    }
    private fun setupTabs() {
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
    }
    private fun iniLivedata() {
        vm.goto.observe(this, { goto ->
            drawerLayout.closeDrawers()
            when(goto) {
                MainViewModel.GOTO.Login -> {
                    goLogin()
                }
                MainViewModel.GOTO.Exit -> {
                    finish()
                }
                MainViewModel.GOTO.PrivacyPolicy -> {
                    intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://cesweb-ef91a.firebaseapp.com"))
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}