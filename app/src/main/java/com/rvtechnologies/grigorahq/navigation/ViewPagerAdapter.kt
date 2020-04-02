package com.rvtechnologies.grigorahq.navigation

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.rvtechnologies.grigorahq.orders.OnGoingOrderFragment
import com.rvtechnologies.grigorahq.orders.OrderFragment
import com.rvtechnologies.grigorahq.orders.PastOrderFragment

class ViewPagerAdapter(
    val context: Context,
    fm: FragmentManager,
    var totalTabs: Int
) : FragmentStatePagerAdapter(fm) {

    // this is for fragment tabs
    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> {
                //  val homeFragment: HomeFragment = HomeFragment()
                return OrderFragment.newInstance("")
            }
            1 -> {
                return OnGoingOrderFragment.newInstance()
            }
            2 -> {
                // val movieFragment = MovieFragment()
                return PastOrderFragment.newInstance()
            }
            else -> return null!!
        }
    }

    // this counts total number of tabs
    override fun getCount(): Int {
        return totalTabs
    }
}

