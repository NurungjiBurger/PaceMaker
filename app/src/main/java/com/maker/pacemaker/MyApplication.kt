package com.maker.pacemaker

import android.app.Application
import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {
    companion object {
        private lateinit var instance: MyApplication
        lateinit var auth: FirebaseAuth
        lateinit var db: FirebaseFirestore
        lateinit var storage: FirebaseStorage
        var email: String? = null

        fun getContext(): Context {
            return instance
        }

        fun checkAuth(): Boolean {
            val currentUser = auth.currentUser
            return currentUser?.let {
                email = currentUser.email
                if (currentUser.isEmailVerified) {
                    true
                } else {
                    false
                }
            } ?: let {
                false
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        instance = this
        auth = Firebase.auth
        db = FirebaseFirestore.getInstance()
        storage = Firebase.storage

    }
}