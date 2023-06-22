package com.example.finalapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.finalapplication.databinding.ActivityAuthBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider

class AuthActivity : AppCompatActivity() {
    lateinit var binding: ActivityAuthBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        supportActionBar?.title="로그인/회원가입"

        changeVisibility(intent.getStringExtra("data").toString())  // 처음 상태: 로그아웃된 상태 -> intent로 상태를 가져옴. intent.getStringExtra("data").toString()

        binding.goSignInBtn.setOnClickListener{
            // 회원가입
            changeVisibility("signin")
        }

        binding.signBtn.setOnClickListener {
            //이메일,비밀번호 회원가입........................ 20210841@duksung.ac.kr  soyoonn
            val email:String = binding.authEmailEditView.text.toString()
            val password:String = binding.authPasswordEditView.text.toString()
            MyApplication.auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this){ task ->
                    if(task.isSuccessful){
                        MyApplication.auth.currentUser?.sendEmailVerification()?.addOnCompleteListener{
                                sendTask ->
                            if(sendTask.isSuccessful){
                                Toast.makeText(baseContext, "회원가입 성공..이메일 확인", Toast.LENGTH_LONG).show()
                                changeVisibility("logout")
                            }
                            else{ // 이메일 잘못 입력 ...
                                Toast.makeText(baseContext, "메일 전송 실패...", Toast.LENGTH_LONG).show()
                                changeVisibility("logout")
                            }
                        }
                    }
                    else{
                        Toast.makeText(baseContext, "회원가입 실패..", Toast.LENGTH_LONG).show()
                        changeVisibility("logout")
                    }
                    binding.authEmailEditView.text.clear()
                    binding.authPasswordEditView.text.clear()
                }
        }

        binding.loginBtn.setOnClickListener {
            //이메일, 비밀번호 로그인.......................
            val email:String = binding.authEmailEditView.text.toString()
            val password:String = binding.authPasswordEditView.text.toString()
            MyApplication.auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this){task ->
                    if(task.isSuccessful){
                        if(MyApplication.checkAuth()){
                            MyApplication.email = email
                            changeVisibility("login")
                        }
                        else{
                            Toast.makeText(baseContext, "이메일 인증 실패..", Toast.LENGTH_LONG).show()
                        }
                    }
                    else {
                        Toast.makeText(baseContext, "로그인 실패..", Toast.LENGTH_LONG).show()
                        changeVisibility("logout")
                    }
                    binding.authEmailEditView.text.clear()
                    binding.authPasswordEditView.text.clear()
                }

        }

        binding.logoutBtn.setOnClickListener {
            //로그아웃...........
            MyApplication.auth.signOut()
            MyApplication.email = null
            changeVisibility("logout")
            finish()
        }

        val requestLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            // ApiException : Google Play 서비스 호출이 실패했을 때 태스크에서 반환할 예외
            try{
                val account = task.getResult(ApiException::class.java)
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                MyApplication.auth.signInWithCredential(credential)
                    .addOnCompleteListener(this){task ->
                        if(task.isSuccessful){
                            MyApplication.email = account.email
                            changeVisibility("login")
                            Log.d("mobileApp", "GoogleSignIn - Successful")
                            finish()
                        }else{
                            changeVisibility("logout")
                            Log.d("mobileApp", "GoogleSignIn - NOT Successful")
                        }
                    }// 인증이 완료되었을 때의 처리
            }catch(e: ApiException){
                changeVisibility("logout")
                Log.d("mobileApp", "GoogleSignIn - ${e.message}")
            }
        }
        binding.googleLoginBtn.setOnClickListener {
            //구글 로그인....................
            val gso : GoogleSignInOptions = GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            val signInIntent : Intent = GoogleSignIn.getClient(this, gso).signInIntent
            requestLauncher.launch(signInIntent)
        }
    }

    fun changeVisibility(mode: String){
        if(mode.equals("signin")){  // 회원가입
            binding.run {
                logoutBtn.visibility = View.GONE
                goSignInBtn.visibility = View.GONE
                googleLoginBtn.visibility = View.GONE
                authEmailEditView.visibility = View.VISIBLE
                authPasswordEditView.visibility = View.VISIBLE
                signBtn.visibility = View.VISIBLE
                loginBtn.visibility = View.GONE
            }
        }else if(mode.equals("login")){
            binding.run { // 로그인된 상태
                authMainTextView.text = "${MyApplication.email} 님 반갑습니다."
                logoutBtn.visibility= View.VISIBLE
                goSignInBtn.visibility= View.GONE
                googleLoginBtn.visibility= View.GONE
                authEmailEditView.visibility= View.GONE
                authPasswordEditView.visibility= View.GONE
                signBtn.visibility= View.GONE
                loginBtn.visibility= View.GONE
            }

        }else if(mode.equals("logout")){ // 로그아웃된 상태
            binding.run {
                authMainTextView.text = "로그인 하거나 회원가입 해주세요."
                logoutBtn.visibility = View.GONE
                goSignInBtn.visibility = View.VISIBLE
                googleLoginBtn.visibility = View.VISIBLE
                authEmailEditView.visibility = View.VISIBLE
                authPasswordEditView.visibility = View.VISIBLE
                signBtn.visibility = View.GONE
                loginBtn.visibility = View.VISIBLE
            }
        }
    }



}