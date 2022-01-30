package com.example.todolist

import android.content.Context
import android.content.res.Resources
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.todolist.databinding.ToastCustomBinding

object CustomToast {
    fun createToast(context: Context, message: String): Toast? {
        val inflater = LayoutInflater.from(context)
        val binding: ToastCustomBinding =
            DataBindingUtil.inflate(inflater, R.layout.toast_custom, null, false)

        binding.tvSample.text = message
        when(message){
            "취소했어요.." -> binding.ivSample.setImageResource(R.drawable.frustrated)
            "완료했어요!!" -> binding.ivSample.setImageResource(R.drawable.happy)

        }

        return Toast(context).apply {
            setGravity(Gravity.BOTTOM, 0, 70.toPx())
            duration = Toast.LENGTH_SHORT
            view = binding.root
        }
    }

    private fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()
}