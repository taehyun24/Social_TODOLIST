package com.example.todolist.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.todolist.databinding.FragmentSocialBinding
import com.google.android.material.tabs.TabLayoutMediator


class SocialFragment: Fragment() {

    private var binding: FragmentSocialBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSocialBinding.inflate(inflater,container,false)

        //페이지 프래그먼트 가져옴
        val list = listOf(HomeFragment(),SocialFragment(),ProfileFragment())
        //어댑터 생성
        val pagerAdapter = FragmentPagerAdapter(list,requireActivity())
        //어댑터와 뷰페이저 연결
        binding?.viewPager?.adapter = pagerAdapter
        //탭 제목 생성
        val titles = listOf("검색","팔로우","팔로워")
        //탭레이아웃과 뷰페이저 연결
        TabLayoutMediator(binding?.tabLayout!!, binding?.viewPager!!){ tab, position ->
            tab.text = titles[position]
        }.attach()
        return binding!!.root
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}

class FragmentPagerAdapter(val fragmentList: List<Fragment>, fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity){

    override fun getItemCount() = fragmentList.size

    override fun createFragment(position: Int) = fragmentList[position]
}