package com.example.todolist.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todolist.fragments.HomeFragment
import com.example.todolist.model.Memo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MemoViewModel : ViewModel() {
    var db: FirebaseFirestore? = null
    var currentUserUid: String? = null
    var auth: FirebaseAuth? = null
    var uid: String? = null
    var memoList: ArrayList<Memo> = arrayListOf()

    private val _currentValue = MutableLiveData<ArrayList<Memo>>()
    val currentValue: LiveData<ArrayList<Memo>>
        get() = _currentValue

    //초기화
    init {
        auth = FirebaseAuth.getInstance()   //유저값 가져옴
        db = FirebaseFirestore.getInstance()    //db 가져옴

        //파이어베이스에서 데이터 가져오기
        db?.collection("memo")?.addSnapshotListener { value, error ->
            for (snapshot in value!!.documents) {
                memoList.add(snapshot.toObject(Memo::class.java)!!)     //파이어베이스에서 가져온값을 memoList에 넣음
            }
            Log.d("크기", memoList.size.toString())
            _currentValue.value = memoList  //초기값 = 파이어베이스에서 가져온 값
        }
    }

    fun updateValue(name: String, time: String, day: String) {

        var memo = Memo()
        memo.uid = auth?.currentUser?.uid
        memo.userEmail = auth?.currentUser?.email
        memo.name = name
        memo.time = time
        memo.day = day
        memo.timestamp = System.currentTimeMillis()
        db?.collection("memo")?.document()?.set(memo)   //파이어베이스에 데이터 추가
        _currentValue.value?.add(memo)
        Log.d("성공", "성공")
    }
}