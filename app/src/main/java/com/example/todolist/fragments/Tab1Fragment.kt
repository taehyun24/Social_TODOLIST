package com.example.todolist.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.MemoDetailActivity
import com.example.todolist.R
import com.example.todolist.databinding.FragmentTab1Binding
import com.example.todolist.model.Memo
import com.example.todolist.model.Profile
import com.example.todolist.viewmodel.MemoViewModel
import com.example.todolist.viewmodel.MemoViewModelFactory
import com.example.todolist.viewmodel.ProfileViewModel

class Tab1Fragment : Fragment() {
    lateinit var profileViewModel: ProfileViewModel
    var binding: FragmentTab1Binding? = null
    lateinit var memoViewModelFactory: MemoViewModelFactory
    lateinit var memoViewModel: MemoViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTab1Binding.inflate(inflater,container,false)
        profileViewModel = ViewModelProvider(requireActivity()).get(ProfileViewModel::class.java)
        profileViewModel.profilecurrentValue.observe(requireActivity(), Observer {
            binding?.tabRecyclerView?.adapter = CustomAdapter()
            binding?.tabRecyclerView?.layoutManager = LinearLayoutManager(activity)
        })


        return binding!!.root
    }

    inner class CustomAdapter : RecyclerView.Adapter<CustomAdapter.ItemViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            var view =
                LayoutInflater.from(parent.context).inflate(R.layout.tab_recycler, parent, false)

            return ItemViewHolder(view)
        }

        inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val name_tv = itemView.findViewById<TextView>(R.id.name_tv)
            val home_btn = itemView.findViewById<Button>(R.id.home_btn)
            fun bind(profile: Profile) {
                name_tv.text = profile.email
            }
        }

        override fun getItemCount(): Int {
            Log.d("숫자", profileViewModel.profileList.size.toString())
            return profileViewModel.profileList.size
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            holder.bind(profileViewModel.profileList[position])
            holder.home_btn.setOnClickListener {

                var bundle = Bundle()
                bundle.putString("uid",profileViewModel.profileList[position].email)
                Navigation.findNavController(binding!!.root).navigate(R.id.action_socialFragment_to_homeFragment,bundle)
            }
        }
    }
}