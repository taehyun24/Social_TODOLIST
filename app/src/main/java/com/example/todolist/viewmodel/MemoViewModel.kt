package com.example.todolist.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todolist.fragments.HomeFragment
import com.example.todolist.model.Memo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MemoViewModel(email: String,uid: String) : ViewModel() {
    var db: FirebaseFirestore? = null
    var currentUserUid: String? = null
    var auth: FirebaseAuth? = null
    var memoList: ArrayList<Memo> = arrayListOf()
    var email = email
    var uid = uid

    private val _currentValue = MutableLiveData<ArrayList<Memo>>()
    val currentValue: LiveData<ArrayList<Memo>>
        get() = _currentValue



    //초기화
    init {
        auth = FirebaseAuth.getInstance()   //유저값 가져옴
        db = FirebaseFirestore.getInstance()    //db 가져옴

        //파이어베이스에서 데이터 가져오기
        db?.collection("memo")?.whereEqualTo("uid",uid)?.addSnapshotListener { value, error ->   //auth?.currentUser?.uid 사용자정보, db에 새로운 값이 들어오면 실행됨
            memoList.clear()
            for (snapshot in value!!.documents) {
                memoList.add(snapshot.toObject(Memo::class.java)!!)     //파이어베이스에서 가져온값을 memoList에 넣음
            }
            Log.d("크기", memoList.size.toString())
            _currentValue.value = memoList  //초기값 = 파이어베이스에서 가져온 값
            Log.d("현재", _currentValue.value?.toString()!!)
        }
    }

    fun updateValue(name: String, time: String, day: String) {
        var memo = Memo()
        memo.uid = uid
        memo.userEmail = email
        memo.name = name
        memo.time = time
        memo.date = day
        memo.timestamp = System.currentTimeMillis()
        db?.collection("memo")?.document()?.set(memo)   //파이어베이스에 데이터 추가하면 위에 함수가 실행되면서 알아서 _currentValue.value값이 바뀜.
        Log.d("성공", "성공")
    }

}