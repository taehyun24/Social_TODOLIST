package com.example.todolist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.todolist.databinding.ActivityLoginBinding
import com.example.todolist.viewmodel.ProfileViewModel
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import java.lang.IllegalArgumentException

class LoginActivity : AppCompatActivity() {

    var auth: FirebaseAuth? = null
    var binding: ActivityLoginBinding? = null
    var googleSignInClient: GoogleSignInClient? = null
    var google_login_code = 9001
    lateinit var profileViewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainBinding = ActivityLoginBinding.inflate(layoutInflater)
        binding = mainBinding
        setContentView(binding!!.root)

        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        auth = FirebaseAuth.getInstance()
        binding?.loginBtn?.setOnClickListener {
            login()
        }
        binding?.signupBtn?.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        //구글 로그인
        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("1069157049850-ac1tscegvsgluh9a3e7hkllmpf9jnvld.apps.googleusercontent.com")
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding?.googleLoginBtn?.setOnClickListener {
            googleLogin()
        }
    }

    fun login() {
        try {
            auth?.signInWithEmailAndPassword(
                binding?.editEmail?.text.toString(),
                binding?.editPw?.text.toString()
            )
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        moveMainPage(task.result?.user)
                    } else {
                        CustomToast.createToast(this, "아이디 또는 비밀번호가 틀렸습니다.")?.show()
                    }
                }
        } catch (e: IllegalArgumentException) {
            CustomToast.createToast(this, "아이디 또는 비밀번호를 입력해주세요.")?.show()
        }

    }

    fun moveMainPage(user: FirebaseUser?) {
        if (user != null) {
            var intent = Intent(this, MainActivity::class.java)
            intent.putExtra("email", user.email)
            intent.putExtra("uid", user.uid)
            startActivity(intent)
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        moveMainPage(auth?.currentUser)
    }

    fun googleLogin() {
        var signInIntent = googleSignInClient?.signInIntent
        startActivityForResult(signInIntent, google_login_code)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == google_login_code) {
            var result = Auth.GoogleSignInApi.getSignInResultFromIntent(data!!)
            if (result!!.isSuccess) {
                var account = result.signInAccount
                firebaseAuthWithGoogle(account)
            }
        }
    }

    fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        var credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        auth?.signInWithCredential(credential)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    profileViewModel.updateValue(
                        task.result?.user?.email!!,
                        task.result?.user?.uid!!,
                        task.result?.user?.displayName!!
                    )
                    moveMainPage(task.result?.user)
                } else {
                    CustomToast.createToast(this, task.exception?.message.toString())?.show()
                }
            }
    }
}