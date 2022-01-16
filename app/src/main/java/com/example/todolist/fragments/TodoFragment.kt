package com.example.todolist.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.todolist.databinding.FragmentTodoBinding
import com.example.todolist.model.Memo
import com.example.todolist.viewmodel.MemoViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class TodoFragment : Fragment() {

    var db: FirebaseFirestore? = null   //데이터베이스 사용
    var auth: FirebaseAuth? = null //유저 정보가져오기 위해 사용
    lateinit var memoViewModel: MemoViewModel

    private var binding: FragmentTodoBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTodoBinding.inflate(inflater, container, false)

        memoViewModel = ViewModelProvider(requireActivity()).get(MemoViewModel::class.java)

        //초기화
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        binding?.createButton?.setOnClickListener {
            var name = binding?.etName?.text.toString()
            var time = binding?.etTime?.text.toString()
            var day = binding?.etDay?.text.toString()
            memoViewModel.updateValue(name,time,day)
            Toast.makeText(requireContext(),"추가되었습니다.",Toast.LENGTH_LONG).show()

        }

        return binding!!.root

    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}