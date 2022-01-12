package com.example.todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.viewpager2.widget.ViewPager2
import com.example.todolist.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 페이저에 어댑터 연결
        binding.viewpager.adapter = ViewPagerAdapter(this)

        // 슬라이드하여 페이지가 변경되면 바텀네비게이션의 탭도 그 페이지로 활성화
        binding.viewpager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.bottomNav.menu.getItem(position).isChecked = true
                }
            }
        )
        // 리스너 연결
        binding.bottomNav.setOnNavigationItemSelectedListener(this)

        try {
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.viewpager) as NavHostFragment    //null이면 navhostFragment가 될 수없기 때문에 트라이캐치문을 써줌
            val navController = navHostFragment.navController
            NavigationUI.setupWithNavController(binding.bottomNav, navController)
        }catch (e: NullPointerException){
            e.printStackTrace()
        }


        binding.bottomNav.itemIconTintList = null   //아이콘 원래 색으로해줌
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.homeFragment -> {
                // ViewPager의 현재 item에 첫 번째 화면을 대입
                binding.viewpager.currentItem = 0
                return true
            }
            R.id.socialFragment -> {
                // ViewPager의 현재 item에 두 번째 화면을 대입
                binding.viewpager.currentItem = 1
                return true
            }
            R.id.profileFragment -> {
                // ViewPager의 현재 item에 세 번째 화면을 대입
                binding.viewpager.currentItem = 2
                return true
            }
            else -> {
                return false
            }
        }
    }
}