package com.example.todolist

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.todolist.databinding.ActivitySignUpBinding
import com.example.todolist.viewmodel.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Pattern

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
            //빈칸이 있을경우
            if (binding?.editEmail?.text?.isEmpty()!! || binding?.editPw?.text?.isEmpty()!! || binding?.editNickName?.text?.isEmpty()!!) {
                CustomToast.createToast(this, "빈칸 없이 입력해주세요.")?.show()
            } else {
                signup()
            }

        }
    }


    fun signup() {
        auth?.createUserWithEmailAndPassword(
            binding?.editEmail?.text.toString(),
            binding?.editPw?.text.toString()
        )
            ?.addOnCompleteListener { task ->
                var pattern: Pattern = android.util.Patterns.EMAIL_ADDRESS

                //비밀번호가 6자 이하일경우
                if (binding?.editPw?.text?.length!! < 6) {
                    CustomToast.createToast(this, "비밀번호를 6자 이상으로 설정해주세요.")?.show()
                }
                //이메일 형식이 아닐경우
                else if (!pattern.matcher(binding?.editEmail?.text?.toString()).matches()) {
                    CustomToast.createToast(this, "이메일 형식으로 입력해주세요.")?.show()
                }
                //모두 제대로 작성했을경우
                else {
                    if (task.isSuccessful) {
                        CustomToast.createToast(this, "성공적으로 회원가입이 되었습니다.")?.show()
                        profileViewModel.updateValue(
                            binding?.editEmail?.text.toString(),
                            auth?.currentUser?.uid!!, binding?.editNickName?.text.toString()
                        )
                        finish()
                    }
                    //아이디가 이미 있는경우
                    else {
                        CustomToast.createToast(this, "중복된 이메일입니다.")?.show()
                    }
                }

            }
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}