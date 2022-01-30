package com.example.todolist.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.todolist.ExplainActivity
import com.example.todolist.MemoDetailActivity
import com.example.todolist.R
import com.example.todolist.databinding.FragmentProfileBinding
import com.example.todolist.viewmodel.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment: Fragment() {

    lateinit var profileViewModel: ProfileViewModel
    private var binding: FragmentProfileBinding? = null
    var auth: FirebaseAuth? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater,container,false)
        auth = FirebaseAuth.getInstance()

        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        profileViewModel.profilecurrentValue.observe(requireActivity(), Observer {
            for (i: Int in 0 until profileViewModel.profileList.size){
                //내계정일경우
                if (profileViewModel.profileList[i].uid?.equals(auth?.currentUser?.uid)!!){
                    binding?.follownNum?.text = profileViewModel.profileList[i].followingCount.toString()
                    binding?.followerNum?.text = profileViewModel.profileList[i].followerCount.toString()
                    binding?.textGauge?.text = "${profileViewModel.profileList[i].gauge_value}p/100p"
                    binding?.progressBar?.setProgress(profileViewModel.profileList[i].gauge_value)
                }
            }
        })

        binding?.explainBtn?.setOnClickListener {
            var intent = Intent(requireContext(), ExplainActivity::class.java)
            startActivity(intent)
        }
        return binding!!.root
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}