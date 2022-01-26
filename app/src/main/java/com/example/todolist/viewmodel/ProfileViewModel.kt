package com.example.todolist.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todolist.model.Profile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileViewModel: ViewModel() {
    var db: FirebaseFirestore? = null
    var profileList: ArrayList<Profile> = arrayListOf()
    var auth: FirebaseAuth? = null
    var idList: ArrayList<String> = arrayListOf()
    var id: String? = null

    private val _profilecurrentValue = MutableLiveData<ArrayList<Profile>>()
    val profilecurrentValue: LiveData<ArrayList<Profile>>
        get() = _profilecurrentValue

    init {
        db = FirebaseFirestore.getInstance()    //db 가져옴
        auth = FirebaseAuth.getInstance()

        db?.collection("profile")?.addSnapshotListener { value, error ->
            profileList.clear()
            for (snapshot in value!!.documents){
                profileList.add(snapshot.toObject(Profile::class.java)!!)
                idList.add(snapshot.id)
            }
            _profilecurrentValue.value = profileList
        }
    }

    fun updateValue(email: String, uid: String){
        var profile = Profile()
        profile.email = email
        profile.uid = uid
        db?.collection("profile")?.document()?.set(profile)
    }

    fun requestFollow(uid: String){
        //내 계정에 상대방 팔로우하는 부분
        /*db?.collection("profile")?.whereEqualTo("uid",auth?.currentUser?.uid)?.addSnapshotListener { value, error ->
            for (snapshot in value!!.documents){
                id = snapshot.id
            }
        }
        Log.d("아디", id!!)
        for (i: Int in 0 until idList.size){
            if(db?.document(idList[i])?.collection("uid")?.equals(auth?.currentUser?.uid)!!){
                id = idList[i]

            }
        }*/
        var tsDocFollowing = db?.collection("profile")?.document(idList[0])
        db?.runTransaction { transaction->
            var followDTO = transaction.get(tsDocFollowing!!).toObject(Profile::class.java)
            Log.d("디티", followDTO.toString())
            /*
            if (followDTO == null){
                followDTO = Profile()
                followDTO!!.followingCount = 1
                followDTO!!.followers[uid] = true

                transaction.set(tsDocFollowing,followDTO)
                return@runTransaction
            }*/

            //상대방을 이미 팔로우했을경우
            if (followDTO!!.followings.containsKey(uid)){
                //팔로잉 취소
                followDTO!!.followingCount -= 1
                followDTO!!.followers.remove(uid)
            }
            else{
                //팔로우 진행
                followDTO.followingCount += 1
                followDTO!!.followers[uid] = true
            }
            transaction.set(tsDocFollowing,followDTO!!)
            return@runTransaction
        }
        // 상대방 계정의 팔로워에 내 계정을 추가하는 부분
        var tsDocFollower = db?.collection("profile")?.document(uid)
        db?.runTransaction { transaction->
            var followDTO = transaction.get(tsDocFollower!!).toObject(Profile::class.java)

            /*
            if (followDTO == null){
                followDTO = Profile()
                followDTO!!.followingCount = 1
                followDTO!!.followers[idList[0]] = true

                transaction.set(tsDocFollower,followDTO!!)
                return@runTransaction
            }*/

            //상대방의 팔로워에 내 계정이 포함되어 있을경우
            if (followDTO!!.followers.containsKey(idList[0])) {
                followDTO!!.followerCount -= 1
                followDTO!!.followers.remove(idList[0])
            }else{
                followDTO!!.followerCount += 1
                followDTO!!.followers[idList[0]] = true
            }
            transaction.set(tsDocFollower,followDTO!!)
            return@runTransaction
        }
    }
}