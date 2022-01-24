package com.example.todolist

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.databinding.ActivityMemoDetailBinding
import com.example.todolist.viewmodel.ProfileViewModel

class MemoDetailActivity: AppCompatActivity() {

    var binding: ActivityMemoDetailBinding? = null
    lateinit var detail_name: String
    lateinit var uidList: ArrayList<String>
    lateinit var profileViewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE) //타이틀 없애기
        binding = ActivityMemoDetailBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        detail_name = intent?.getStringExtra("name").toString()
        uidList = intent?.getStringArrayListExtra("userID")!!   //넘어온 uid값

       profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        profileViewModel.profilecurrentValue.observe(this, Observer {
            binding?.memoDetailRecyclerView?.adapter = MemoDetailAdapter()
            binding?.emoDetailRecyclerView?.adapter = EmoticonAdapter()
            binding?.memoDetailRecyclerView?.layoutManager = LinearLayoutManager(this)
            binding?.emoDetailRecyclerView?.layoutManager = LinearLayoutManager(this)
        })
    }

    inner class MemoDetailAdapter : RecyclerView.Adapter<MemoDetailAdapter.ItemViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            var view =
                LayoutInflater.from(parent.context).inflate(R.layout.memo_detail_item, parent, false)

            return ItemViewHolder(view)
        }

        inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val memo_detail_name = itemView.findViewById<TextView>(R.id.memo_detail_name)

            fun bind(name: String) {
                memo_detail_name.text = name
            }

        }

        override fun getItemCount(): Int {
            return 1
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            holder.bind(detail_name)
        }
    }

    inner class EmoticonAdapter : RecyclerView.Adapter<EmoticonAdapter.ItemViewHolder>(){

        var userList: ArrayList<String> = arrayListOf()
        init {
            for (i: Int in 0 until uidList.size){
                for (q: Int in 0 until profileViewModel.profileList.size){
                    if(uidList[i].equals(profileViewModel.profileList[q].uid)){
                        userList.add(profileViewModel.profileList[q].email!!)
                        break
                    }
                }
            }
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmoticonAdapter.ItemViewHolder
        {
            var view =
                LayoutInflater.from(parent.context).inflate(R.layout.emo_detail_item,parent,false)
            return ItemViewHolder(view)
        }

        inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            private val userName = itemView.findViewById<TextView>(R.id.username)
            private val emoticon = itemView.findViewById<ImageView>(R.id.emoticon)

            fun bind(user: String){
                userName.text = user
                emoticon.setImageResource(R.drawable.cheer_up_edit)
            }
        }

        override fun onBindViewHolder(holder: EmoticonAdapter.ItemViewHolder, position: Int) {
            holder.bind(userList[position])
        }

        override fun getItemCount(): Int {
            return userList.size
        }

    }


    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}