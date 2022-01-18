package com.example.todolist.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.todolist.databinding.FragmentTodoBinding
import com.example.todolist.model.Memo
import com.example.todolist.viewmodel.MemoViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class TodoFragment : BottomSheetDialogFragment() {

    lateinit var memoViewModel: MemoViewModel

    private var binding: FragmentTodoBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTodoBinding.inflate(inflater, container, false)

           //현재날짜
        var date = arguments?.get("date").toString()
        binding?.tvDate?.text = date
        memoViewModel = ViewModelProvider(requireActivity()).get(MemoViewModel::class.java)


        binding?.createButton?.setOnClickListener {
            var name = binding?.etName?.text.toString()
            var time = binding?.etTime?.text.toString()
            memoViewModel.updateValue(name,time, date)
            Toast.makeText(requireContext(),"추가되었습니다.",Toast.LENGTH_LONG).show()

        }

        return binding!!.root

    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}