package com.maker.pacemaker.data.model.remote

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

class AuthInterceptor(context: Context) : Interceptor {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)

    override fun intercept(chain: Interceptor.Chain): Response {
        // SharedPreferences에서 FCM 토큰 가져오기 ( JWT로 사용 )
        var accessToken = sharedPreferences.getString("accessToken", null)

        Log.d("FCM", "토큰: $accessToken")

        // 요청을 빌드
        val requestBuilder = chain.request().newBuilder()
        if (accessToken != null) {
            requestBuilder.addHeader("Authorization", "Bearer $accessToken")
        }

        var response = chain.proceed(requestBuilder.build())

        // 401 Unauthorized 오류가 발생하면 토큰 갱신 시도
        if (response.code == 401) {
            // 여기서 토큰 갱신 로직 추가
            accessToken = refreshToken() // 새로운 토큰을 받아오는 함수

            if (accessToken != null) {
                // 새로운 토큰으로 다시 요청 보내기
                sharedPreferences.edit().putString("accessToken", accessToken).apply()
                requestBuilder.addHeader("Authorization", "Bearer $accessToken")
                response = chain.proceed(requestBuilder.build())
            }
        }

        Log.d("FCM", "보냈어 토큰 : $accessToken")

        return response
    }

    // 토큰 갱신을 위한 메소드 예시
    private fun refreshToken(): String? {
        Log.d("AuthInterceptor", "토큰 갱신 시도")

        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            Log.e("identifyUserByToken", "사용자가 로그인되지 않았습니다.")
            return null
        }

        // 비동기 작업 완료를 대기하기 위한 CountDownLatch 생성
        val latch = java.util.concurrent.CountDownLatch(1)
        var newToken: String? = null

        user.getIdToken(true).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                newToken = task.result?.token
                Log.d("AuthInterceptor", "새로운 토큰: $newToken")
            } else {
                Log.e("AuthInterceptor", "토큰 갱신 실패: ${task.exception}")
            }
            latch.countDown() // 작업 완료 시 latch 감소
        }

        // 비동기 작업이 완료될 때까지 대기
        latch.await()

        return newToken
    }
}