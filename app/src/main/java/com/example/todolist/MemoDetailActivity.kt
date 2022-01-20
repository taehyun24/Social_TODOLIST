package com.example.todolist

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.databinding.ActivityMemoDetailBinding

class MemoDetailActivity: Activity() {

    var binding: ActivityMemoDetailBinding? = null
    lateinit var detail_name: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)   //타이틀 없애기
        binding = ActivityMemoDetailBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        detail_name = intent?.getStringExtra("name").toString()
        Log.d("ds","ds")
        binding?.memoDetailRecyclerView?.adapter = MemoDetailAdapter()
        binding?.memoDetailRecyclerView?.layoutManager = LinearLayoutManager(this)
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

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}