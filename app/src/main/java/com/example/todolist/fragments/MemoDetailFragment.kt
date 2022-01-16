package com.example.todolist.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.databinding.FragmentMemoDetailBinding
import com.example.todolist.model.Memo
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MemoDetailFragment: BottomSheetDialogFragment() {

    var binding: FragmentMemoDetailBinding? = null
    lateinit var detail_name: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMemoDetailBinding.inflate(inflater,container,false)
        var bundle = arguments
        detail_name = bundle?.getString("name","default").toString()
        Log.d("ds","ds")
        binding?.memoDetailRecyclerView?.adapter = MemoDetailAdapter()
        binding?.memoDetailRecyclerView?.layoutManager = LinearLayoutManager(activity)
        return binding!!.root
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
            return 2
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            holder.bind(detail_name)
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}