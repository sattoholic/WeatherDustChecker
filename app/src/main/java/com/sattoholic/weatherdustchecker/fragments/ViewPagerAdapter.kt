package com.sattoholic.weatherdustchecker.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {
    private var fragmentList = mutableListOf<Fragment>()

    fun setFragments(fragmentList: MutableList<Fragment>) {
        this.fragmentList.clear()
        this.fragmentList = fragmentList
        notifyDataSetChanged()
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }


    override fun getItemCount(): Int = fragmentList.size
}