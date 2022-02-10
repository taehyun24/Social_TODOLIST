package com.example.todolist.fragments

import android.R
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
import android.widget.TimePicker

import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener


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
        binding?.tvDate?.text = "날짜: ${date}"

        memoViewModelFactory = MemoViewModelFactory(email!!, uid!!,date)
        memoViewModel = ViewModelProvider(this,memoViewModelFactory).get(MemoViewModel::class.java)

        binding?.etTime?.setOnClickListener {
            val dialog = TimePickerDialog(
                requireContext(),
                R.style.Theme_Holo_Light_Dialog_NoActionBar,
                listener,
                15,
                24,
                false
            )
            dialog.setTitle("대여반납시간")
            dialog.window!!.setBackgroundDrawableResource(R.color.transparent)
            dialog.show()
        }


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

    private val listener =
        OnTimeSetListener { view, hourOfDay, minute ->
                binding?.etTime?.textSize = 15F
                binding?.etTime?.setText("$hourOfDay:$minute")
            }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}