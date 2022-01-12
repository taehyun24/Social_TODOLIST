package com.example.todolist

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.todolist.fragments.HomeFragment
import com.example.todolist.fragments.ProfileFragment
import com.example.todolist.fragments.SocialFragment

class ViewPagerAdapter (fragment : FragmentActivity) : FragmentStateAdapter(fragment){
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment()
            1 -> SocialFragment()
            else -> ProfileFragment()
        }
    }
}