package com.asiauniv.myapplication

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm){

    val pages = ArrayList<Fragment>()
    val tabs = ArrayList<String>()

    fun addFragment(tab: String, page: Fragment){
        tabs.add(tab)
        pages.add(page)

    }

    override fun getItem(position: Int): Fragment {
        return pages[position]
    }

    override fun getCount(): Int {
        return pages.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return tabs[position]
    }
}