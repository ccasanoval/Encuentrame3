package com.cesoft.feature_login.ui

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.cesoft.feature_login.R
import com.cesoft.feature_login.ui.LoginFragment.Companion.ENTER
import com.cesoft.feature_login.ui.LoginFragment.Companion.MAX_PAGES
import com.cesoft.feature_login.ui.LoginFragment.Companion.RECOVER
import com.cesoft.feature_login.ui.LoginFragment.Companion.REGISTER

class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return LoginFragment.newInstance(position)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            ENTER -> context.getString(R.string.enter_lbl)
            REGISTER -> context.getString(R.string.register_lbl)
            RECOVER -> context.getString(R.string.recover_lbl)
            else -> null
        }
    }

    override fun getCount(): Int {
        return MAX_PAGES
    }

    companion object {

    }
}