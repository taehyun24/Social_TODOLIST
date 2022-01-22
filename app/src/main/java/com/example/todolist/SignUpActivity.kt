package com.example.todolist

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.todolist.databinding.ActivitySignUpBinding
import com.example.todolist.viewmodel.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {

    lateinit var profileViewModel: ProfileViewModel
    var auth: FirebaseAuth? = null
    var binding: ActivitySignUpBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        auth = FirebaseAuth.getInstance()
        binding?.signupBtn?.setOnClickListener {
            signup()
        }
    }

    fun signup(){
        auth?.createUserWithEmailAndPassword(binding?.editId?.text.toString(),binding?.editPw?.text.toString())
            ?.addOnCompleteListener {
                    task ->
                if (binding?.editId != null && binding?.editPw != null){
                    if (task.isSuccessful){
                        Toast.makeText(this,"회원가입이 되었습니다.",Toast.LENGTH_LONG).show()
                        profileViewModel.updateValue(binding?.editId?.text.toString())
                        finish()
                    }
                }else{
                    Toast.makeText(this,"빈칸 없이 입력해주세요", Toast.LENGTH_LONG).show()
                }
            }
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}