package com.cesoft.encuentrame3.ui

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.cesoft.encuentrame3.R
import com.cesoft.feature_alert.AlertFragment
import com.cesoft.feature_poi.ui.PoiListFragment
import com.cesoft.feature_route.RouteFragment

class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return when(position) {
            POI -> PoiListFragment.newInstance()
            ROUTE -> RouteFragment.newInstance()
            ALERT -> AlertFragment.newInstance()
            else -> PoiListFragment.newInstance()
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when(position) {
            POI -> context.resources.getString(R.string.pois)
            ROUTE -> context.resources.getString(R.string.routes)
            ALERT -> context.resources.getString(R.string.alert)
            else -> context.resources.getString(R.string.pois)
        }
    }

    override fun getCount(): Int {
        return MAX_PAGES
    }

    companion object {
        const val MAX_PAGES = 3
        const val POI = 0
        const val ROUTE = 1
        const val ALERT = 2
    }
}