package com.example.myapplication

import androidx.multidex.MultiDexApplication
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

// Dex : Dalvic Executable (64K)
class MyApplication : MultiDexApplication() {
    companion object {
        lateinit var auth: FirebaseAuth
        var email: String? = null
        lateinit var db: FirebaseFirestore
        lateinit var storage: FirebaseStorage
        lateinit var appContext: MyApplication

        fun checkAuth(): Boolean {
            val currentUser = auth.currentUser
            if (currentUser != null) {
                email = currentUser.email
                return currentUser.isEmailVerified
            }
            return false
        }
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
        auth = Firebase.auth
        db = FirebaseFirestore.getInstance()
        storage = Firebase.storage
    }
}
