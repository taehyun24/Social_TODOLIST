package com.example.todolist

import android.app.Activity
import android.app.AlertDialog
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
import android.widget.Toast

import android.content.DialogInterface
import android.content.Intent
import com.example.todolist.fragments.HomeFragmentDirections
import com.example.todolist.viewmodel.MemoViewModel
import com.example.todolist.viewmodel.MemoViewModelFactory


class MemoDetailActivity : AppCompatActivity() {

    var binding: ActivityMemoDetailBinding? = null
    lateinit var detail_name: String
    lateinit var detail_time: String
    lateinit var email: String
    lateinit var uidList: ArrayList<String>
    lateinit var profileViewModel: ProfileViewModel
    lateinit var memoViewModelFactory: MemoViewModelFactory
    lateinit var memoViewModel: MemoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE) //타이틀 없애기
        binding = ActivityMemoDetailBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        binding?.deleteBtn?.setOnClickListener {
            showDialog()
        }
        detail_name = intent?.getStringExtra("name").toString()
        detail_time = intent?.getStringExtra("time").toString()
        email = intent?.getStringExtra("email").toString()
        uidList = intent?.getStringArrayListExtra("userID")!!   //넘어온 uid값

        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        memoViewModelFactory = MemoViewModelFactory(email, "ds", "ds")
        memoViewModel = ViewModelProvider(this, memoViewModelFactory).get(MemoViewModel::class.java)


        profileViewModel.profilecurrentValue.observe(this, Observer {
            binding?.memoDetailRecyclerView?.adapter = MemoDetailAdapter()
            binding?.emoDetailRecyclerView?.adapter = EmoticonAdapter()
            binding?.memoDetailRecyclerView?.layoutManager = LinearLayoutManager(this)
            binding?.emoDetailRecyclerView?.layoutManager = LinearLayoutManager(this)
        })
    }

    fun showDialog() {
        val msgBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
            .setTitle("메모 삭제")
            .setMessage("메모를 삭제하시겠습니까?")
            .setPositiveButton("네",
                DialogInterface.OnClickListener { dialogInterface, i ->
                    memoViewModel.deleteValue(detail_name)
                })
            .setNegativeButton("아니오",
                DialogInterface.OnClickListener { dialogInterface, i ->
                    Toast.makeText(
                        this,
                        "취소하였습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                })
        val msgDlg: AlertDialog = msgBuilder.create()
        msgDlg.show()
    }

    inner class MemoDetailAdapter : RecyclerView.Adapter<MemoDetailAdapter.ItemViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            var view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.memo_detail_item, parent, false)

            return ItemViewHolder(view)
        }

        inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val memo_detail_name = itemView.findViewById<TextView>(R.id.memo_detail_name)
            private val memo_detail_time = itemView.findViewById<TextView>(R.id.memo_detail_time)

            fun bind(name: String, time: String) {
                memo_detail_name.text = name
                memo_detail_time.text = time
            }

        }

        override fun getItemCount(): Int {
            return 1
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            holder.bind(detail_name, detail_time)
        }
    }

    inner class EmoticonAdapter : RecyclerView.Adapter<EmoticonAdapter.ItemViewHolder>() {

        var userList: ArrayList<String> = arrayListOf()

        init {
            for (i: Int in 0 until uidList.size) {
                for (q: Int in 0 until profileViewModel.profileList.size) {
                    if (uidList[i].equals(profileViewModel.profileList[q].uid)) {
                        userList.add(profileViewModel.profileList[q].email!!)
                        break
                    }
                }
            }
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): EmoticonAdapter.ItemViewHolder {
            var view =
                LayoutInflater.from(parent.context).inflate(R.layout.emo_detail_item, parent, false)
            return ItemViewHolder(view)
        }

        inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val userName = itemView.findViewById<TextView>(R.id.username)
            private val emoticon = itemView.findViewById<ImageView>(R.id.emoticon)

            fun bind(user: String) {
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