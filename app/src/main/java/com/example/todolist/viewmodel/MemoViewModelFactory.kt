package com.example.todolist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MemoViewModelFactory(val email: String, var uid: String): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MemoViewModel::class.java)){
            return MemoViewModel(email, uid) as T
        }
        throw IllegalArgumentException("Un")
    }
}