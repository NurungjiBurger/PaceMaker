package com.maker.pacemaker

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import androidx.core.app.NotificationCompat

@HiltAndroidApp
class MyApplication : Application() {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

//    override fun getWorkManagerConfiguration(): Configuration {
//        return Configuration.Builder()
//            .setWorkerFactory(workerFactory)
//            .build()
//    }

    companion object {
        private lateinit var instance: MyApplication
        lateinit var auth: FirebaseAuth
        lateinit var db: FirebaseFirestore
        lateinit var storage: FirebaseStorage
        var email: String? = null

        fun getContext(): Context {
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        instance = this
        auth = Firebase.auth
        db = FirebaseFirestore.getInstance()
        storage = Firebase.storage


        // FCM 토큰 가져오기
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                Log.d("FCM", "FCM 토큰: $token")
            } else {
                Log.e("FCM", "FCM 토큰 가져오기 실패: ${task.exception}")
            }
        }
    }
}