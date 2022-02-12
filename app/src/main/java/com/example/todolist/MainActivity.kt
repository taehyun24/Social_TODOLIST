package com.example.todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.todolist.databinding.ActivityMainBinding
import com.example.todolist.fragments.HomeFragment
import com.example.todolist.fragments.TodoFragment
import com.example.todolist.viewmodel.MemoViewModel
import com.example.todolist.viewmodel.MemoViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //네비게이션을 담는 호스트
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        //네비게이션 컨트롤러
        val navController = navHostFragment.navController
        //바텀네비게이션뷰와 네비게이션 컨트롤러를 묶어줌
        NavigationUI.setupWithNavController(binding.bottomNav, navController)


        binding.bottomNav.itemIconTintList = null   //아이콘 원래 색으로해줌
    }
}