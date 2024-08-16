package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityAuthBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse


class AuthActivity : AppCompatActivity() {

    lateinit var binding: ActivityAuthBinding
    private val db = FirebaseFirestore.getInstance()
    private val SEARCH_ADDRESS_ACTIVITY = 10000


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Naver SDK 초기화
        NaverIdLoginSDK.initialize(
            MyApplication.appContext,
            getString(R.string.naver_client_id),
            getString(R.string.naver_client_secret),
            "myapplication"
        )

        changeVisibility(intent.getStringExtra("status").toString())

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.goSignInBtn.setOnClickListener {
            changeVisibility("signin")
        }

        binding.btnSearch.setOnClickListener {
            val intent = Intent(this, DaumAddressActivity::class.java)
            startActivityForResult(intent, SEARCH_ADDRESS_ACTIVITY)
        }

        binding.signBtn.setOnClickListener {
            val email = binding.authEmailEditView.text.toString()
            val password = binding.authPasswordEditView.text.toString()
            MyApplication.auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    binding.authEmailEditView.text.clear()
                    binding.authPasswordEditView.text.clear()
                    binding.place.setText("")
                    binding.placeDetail.setText("")
                    if (task.isSuccessful) {
                        MyApplication.auth.currentUser?.sendEmailVerification()
                            ?.addOnCompleteListener { sendTask ->
                                if (sendTask.isSuccessful) {
                                    Toast.makeText(
                                        this,
                                        "회원가입을 성공했습니다. 메일을 확인해주세요.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    Log.d("mobileapp", "회원가입 성공")
                                    changeVisibility("logout")
                                } else {
                                    Toast.makeText(this, "메일발송을 실패했습니다.", Toast.LENGTH_SHORT)
                                        .show()
                                    Log.d("mobileapp", "메일발송 실패")
                                    changeVisibility("logout")
                                }
                            }
                    } else {
                        Toast.makeText(this, "회원가입을 실패했습니다.", Toast.LENGTH_SHORT).show()
                        Log.d("mobileapp", "== ${task.exception} ==")
                        changeVisibility("logout")
                    }
                }
        }

        binding.loginBtn.setOnClickListener {   // 로그인 Button
            val email = binding.authEmailEditView.text.toString()
            val password = binding.authPasswordEditView.text.toString()
            MyApplication.auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this){task ->
                    binding.authEmailEditView.text.clear()
                    binding.authPasswordEditView.text.clear()
                    if(task.isSuccessful){
                        if(MyApplication.checkAuth()){
                            MyApplication.email = email
                            Toast.makeText(baseContext,"유저 아이디로 로그인하였습니다.",Toast.LENGTH_SHORT).show()
                            Log.d("mobileapp", "로그인을 성공했습니다.")
                            finish()
                        }
                        else{
                            Toast.makeText(baseContext,"이메일 인증이 되지 않았습니다.",Toast.LENGTH_SHORT).show()
                            Log.d("mobileapp", "이메일 인증 안됨")
                        }
                    }
                    else{
                        Toast.makeText(baseContext,"로그인을 실패했습니다.",Toast.LENGTH_SHORT).show()
                        Log.d("mobileapp", "로그인 실패")
                    }
                }
        }

        binding.logoutBtn.setOnClickListener {
            MyApplication.auth.signOut()
            NaverIdLoginSDK.logout()
            MyApplication.email = null
            Log.d("mobileapp", "로그아웃")
            // Activity를 재시작하여 로그아웃 처리를 확인
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }


        val requestLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                Log.d("mobileapp", "account1 : $task")
                try {
                    val account = task.getResult(ApiException::class.java)
                    val crendential = GoogleAuthProvider.getCredential(account.idToken, null)
                    MyApplication.auth.signInWithCredential(crendential)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                MyApplication.email = account.email
                                Toast.makeText(this, "구글로 로그인하였습니다.", Toast.LENGTH_SHORT)
                                    .show()
                                Log.d("mobileapp", "구글 로그인 성공")
                                finish()
                            } else {
                                changeVisibility("logout")
                                Toast.makeText(this, "구글 로그인을 실패했습니다.", Toast.LENGTH_SHORT)
                                    .show()
                                Log.d("mobileapp", "구글 로그인 실패")
                            }
                        }
                } catch (e: ApiException) {
                    changeVisibility("logout")
                    Log.d("mobileapp", "구글 로그인 Exception : ${e.message}, ${e.statusCode}")
                }
            }

        binding.googleLoginBtn.setOnClickListener {
            val gso = GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_client_id))
                .requestEmail()
                .build()
            val signInIntent = GoogleSignIn.getClient(this, gso).signInIntent
            requestLauncher.launch(signInIntent)
        }

        binding.naverLoginBtn.setOnClickListener {
            val oAuthLoginCallback = object : OAuthLoginCallback {
                override fun onSuccess() {
                    NidOAuthLogin().callProfileApi(object : NidProfileCallback<NidProfileResponse> {
                        override fun onSuccess(result: NidProfileResponse) {
                            MyApplication.email = result.profile?.email.toString()
                            Toast.makeText(this@AuthActivity, "네이버로 로그인하였습니다.", Toast.LENGTH_SHORT).show()
                            Log.d("mobileapp", "네이버 로그인 성공")
                            finish()
                        }

                        override fun onError(errorCode: Int, message: String) {
                            Toast.makeText(this@AuthActivity, "네이버 로그인을 실패하였습니다.", Toast.LENGTH_SHORT).show()
                            Log.d("mobileapp", "네이버 로그인 실패")
                        }

                        override fun onFailure(httpStatus: Int, message: String) {
                            Toast.makeText(this@AuthActivity, "네이버 로그인을 실패하였습니다.", Toast.LENGTH_SHORT).show()
                            Log.d("mobileapp", "네이버 로그인 실패")
                        }
                    })
                }

                override fun onError(errorCode: Int, message: String) {
                    Toast.makeText(this@AuthActivity, "네이버 로그인을 실패하였습니다.", Toast.LENGTH_SHORT).show()
                    Log.d("mobileapp", "네이버 로그인 실패")
                }

                override fun onFailure(httpStatus: Int, message: String) {
                    Toast.makeText(this@AuthActivity, "네이버 로그인을 실패하였습니다.", Toast.LENGTH_SHORT).show()
                    Log.d("mobileapp", "네이버 로그인 실패")
                }
            }
            NaverIdLoginSDK.authenticate(this, oAuthLoginCallback)
        }
    }

    fun changeVisibility(mode: String) {
        if (mode == "login") {
            binding.run {
                authMainTextView.text = "정말 로그아웃하시겠습니까?"
                authMainTextView.visibility = View.VISIBLE
                logoutBtn.visibility = View.VISIBLE
                goSignInBtn.visibility = View.GONE
                authEmailEditView.visibility = View.GONE
                authPasswordEditView.visibility = View.GONE
                signBtn.visibility = View.GONE
                loginBtn.visibility = View.GONE
                googleLoginBtn.visibility = View.GONE
                naverLoginBtn.visibility = View.GONE
                btnSearch.visibility = View.GONE
                place.visibility = View.GONE
                placeDetail.visibility = View.GONE
            }
        } else if (mode == "logout") {
            binding.run {
                authMainTextView.text = "로그인 하거나 회원가입 해주세요."
                authMainTextView.visibility = View.VISIBLE
                logoutBtn.visibility = View.GONE
                goSignInBtn.visibility = View.VISIBLE
                authEmailEditView.visibility = View.VISIBLE
                authPasswordEditView.visibility = View.VISIBLE
                signBtn.visibility = View.GONE
                loginBtn.visibility = View.VISIBLE
                googleLoginBtn.visibility = View.VISIBLE
                naverLoginBtn.visibility = View.VISIBLE
                btnSearch.visibility = View.GONE
                place.visibility = View.GONE
                placeDetail.visibility = View.GONE
            }
        } else if (mode == "signin") {
            binding.run {
                authMainTextView.visibility = View.GONE
                logoutBtn.visibility = View.GONE
                goSignInBtn.visibility = View.GONE
                authEmailEditView.visibility = View.VISIBLE
                authPasswordEditView.visibility = View.VISIBLE
                signBtn.visibility = View.VISIBLE
                loginBtn.visibility = View.GONE
                googleLoginBtn.visibility = View.GONE
                naverLoginBtn.visibility = View.GONE
                btnSearch.visibility = View.VISIBLE
                place.visibility = View.VISIBLE
                placeDetail.visibility = View.VISIBLE
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode == SEARCH_ADDRESS_ACTIVITY) {
            if (resultCode == RESULT_OK && intent != null) {
                val data = intent.getStringExtra("data")
                if (data != null) {
                    binding.place.setText(data)
                    saveAddressToFirestore(data)
                }
            }
        }
    }


    private fun saveAddressToFirestore(address: String) {
        val userId = MyApplication.auth.currentUser?.uid
        if (userId != null) {
            val userAddress = hashMapOf(
                "zipcode" to address.substring(0, 5),
                "address" to address.substring(7)
            )

            db.collection("users").document(userId)
                .set(userAddress)
                .addOnSuccessListener {
                    Toast.makeText(this, "주소가 저장되었습니다.", Toast.LENGTH_SHORT).show()
                    binding.place.setText("")
                    binding.placeDetail.setText("")
                }
                .addOnFailureListener { e ->
                    Log.w("AuthActivity", "Error saving address", e)
                    Toast.makeText(this, "주소 저장을 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        val intent = intent
        setResult(Activity.RESULT_OK, intent)
        finish()
        return true
    }

}