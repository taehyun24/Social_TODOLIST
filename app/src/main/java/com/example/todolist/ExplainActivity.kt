package com.example.todolist

import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.example.todolist.databinding.ActivityExplainBinding

class ExplainActivity : AppCompatActivity() {

    var binding: ActivityExplainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE) //타이틀 없애기
        binding = ActivityExplainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
    }
}