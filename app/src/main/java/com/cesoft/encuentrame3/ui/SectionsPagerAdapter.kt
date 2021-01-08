package com.cesoft.encuentrame3.ui

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.cesoft.encuentrame3.R
import com.cesoft.feature_alert.AlertFragment
import com.cesoft.feature_poi.PoiFragment
import com.cesoft.feature_route.RouteFragment

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return when(position) {
            LUGARES -> PoiFragment.newInstance()
            RUTAS -> RouteFragment.newInstance()
            AVISOS -> AlertFragment.newInstance()
            else -> PoiFragment.newInstance()
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        //if(true)context.resources.getString(R.string.app_name)
        return when(position) {
            LUGARES -> context.resources.getString(R.string.lugares)
            RUTAS -> context.resources.getString(R.string.rutas)
            AVISOS -> context.resources.getString(R.string.avisos)
            else -> context.resources.getString(R.string.lugares)
        }
    }

    override fun getCount(): Int {
        return MAX_PAGES
    }

    companion object {
        const val MAX_PAGES = 3
        const val LUGARES = 0
        const val RUTAS = 1
        const val AVISOS = 2
    }
}