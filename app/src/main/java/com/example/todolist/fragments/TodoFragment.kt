package com.example.todolist.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.todolist.CustomToast
import com.example.todolist.databinding.FragmentTodoBinding
import com.example.todolist.viewmodel.MemoViewModel
import com.example.todolist.viewmodel.MemoViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class TodoFragment : BottomSheetDialogFragment() {

    lateinit var memoViewModelFactory: MemoViewModelFactory
    lateinit var memoViewModel: MemoViewModel
    var uid: String? = null
    var email: String? = null
    private var binding: FragmentTodoBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTodoBinding.inflate(inflater, container, false)

           //현재날짜
        var date = arguments?.get("date").toString()
        uid = arguments?.get("uid").toString()
        email = arguments?.get("email").toString()
        binding?.tvDate?.text = date

        memoViewModelFactory = MemoViewModelFactory(email!!, uid!!,date)
        memoViewModel = ViewModelProvider(this,memoViewModelFactory).get(MemoViewModel::class.java)


        binding?.createButton?.setOnClickListener {
            var name = binding?.etName?.text.toString()
            var time = binding?.etTime?.text.toString()
            if(name!=""){
                memoViewModel.updateValue(name,time, date)
                CustomToast.createToast(requireContext(),"추가되었습니다!!")?.show()
            }else{
                CustomToast.createToast(requireContext(),"이름을 입력해주세요.")?.show()
            }
        }

        return binding!!.root

    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}