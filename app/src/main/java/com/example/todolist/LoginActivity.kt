package com.example.todolist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.todolist.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.lang.IllegalArgumentException

class LoginActivity : AppCompatActivity() {

    var auth: FirebaseAuth? = null
    var binding: ActivityLoginBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainBinding = ActivityLoginBinding.inflate(layoutInflater)
        binding = mainBinding
        setContentView(binding!!.root)

        auth = FirebaseAuth.getInstance()
        binding?.loginBtn?.setOnClickListener {
            login()
        }
        binding?.signupBtn?.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
        }
    }

    fun login() {
        try {
            auth?.signInWithEmailAndPassword(
                binding?.editEmail?.text.toString(),
                binding?.editPw?.text.toString())
                ?.addOnCompleteListener {
                        task ->
                    if (task.isSuccessful) {
                        moveMainPage(task.result?.user)
                    } else {
                        Toast.makeText(this, "아이디 또는 비밀번호가 틀렸습니다.", Toast.LENGTH_LONG).show()
                    }
                }
        }catch (e: IllegalArgumentException){
            Toast.makeText(this, "아이디 또는 비밀번호를 입력해주세요.", Toast.LENGTH_LONG).show()
        }

    }

    fun moveMainPage(user: FirebaseUser?){
        if (user != null){
            var intent = Intent(this,MainActivity::class.java)
            intent.putExtra("email",user.email)
            intent.putExtra("uid",user.uid)
            startActivity(intent)
            finish()
        }
    }

    override fun onStart() {
        super.onStart()

    }
}