package com.example.todolist.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todolist.model.Memo
import com.example.todolist.model.Profile
import com.google.firebase.firestore.FirebaseFirestore

class ProfileViewModel: ViewModel() {
    var db: FirebaseFirestore? = null
    var profileList: ArrayList<Profile> = arrayListOf()

    private val _profilecurrentValue = MutableLiveData<ArrayList<Profile>>()
    val profilecurrentValue: LiveData<ArrayList<Profile>>
        get() = _profilecurrentValue

    init {
        db = FirebaseFirestore.getInstance()    //db 가져옴

        db?.collection("profile")?.addSnapshotListener { value, error ->
            profileList.clear()
            for (snapshot in value!!.documents){
                profileList.add(snapshot.toObject(Profile::class.java)!!)
            }
            _profilecurrentValue.value = profileList
        }
    }
    fun updateValue(email: String){
        var profile = Profile()
        profile.email = email
        db?.collection("profile")?.document()?.set(profile)
    }
}