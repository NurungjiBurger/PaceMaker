package com.maker.pacemaker.data.model.remote

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
class AuthInterceptor(context: Context) : Interceptor {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)

    override fun intercept(chain: Interceptor.Chain): Response {
        var idToken = sharedPreferences.getString("idToken", null)

        Log.e("AuthInterceptor", "들어왔어염.")

        // 기존 토큰을 사용하여 요청 빌드
        val requestBuilder = chain.request().newBuilder()
        if (idToken != null) {
            requestBuilder.addHeader("Authorization", "Bearer $idToken")
            Log.d("AuthInterceptor", "기존 토큰으로 요청을 보냅니다: $idToken")
        }

        var response = chain.proceed(requestBuilder.build())

        // 401 Unauthorized 오류가 발생하면 토큰 갱신 시도
        if (response.code == 401) {
            Log.d("AuthInterceptor", "401 오류 발생 - 토큰 갱신 시도 중")

            idToken = runBlocking { refreshToken() } // 새로운 토큰을 받아오는 함수

            if (idToken != null) {
                Log.d("AuthInterceptor", "새로운 토큰을 발급받았습니다: $idToken")
                sharedPreferences.edit().putString("idToken", idToken).apply()
                requestBuilder.removeHeader("Authorization") // 기존 헤더 제거
                requestBuilder.addHeader("Authorization", "Bearer $idToken")
                response.close() // 이전 응답 닫기
                response = chain.proceed(requestBuilder.build())
                Log.d("AuthInterceptor", "갱신된 토큰으로 다시 요청을 보냈습니다.")
            } else {
                Log.e("AuthInterceptor", "토큰 갱신 실패: 새로운 토큰을 가져오지 못했습니다.")
            }
        }

        // 서버에 요청이 성공적으로 전송되었을 때 로그 출력
        if (response.isSuccessful) {
            Log.d("AuthInterceptor", "서버에 요청을 성공적으로 전송했습니다. 응답 코드: ${response.code}")
        } else {
            Log.e("AuthInterceptor", "서버 응답 오류: ${response.code}")
        }

        return response
    }

    // suspend 함수로 변경하여 토큰 갱신을 비동기적으로 처리
    private suspend fun refreshToken(): String? {
        val user = FirebaseAuth.getInstance().currentUser
        return if (user != null) {
            try {
                val newToken = user.getIdToken(true).await().token
                Log.d("AuthInterceptor", "갱신된 토큰: $newToken")
                newToken
            } catch (e: Exception) {
                Log.e("AuthInterceptor", "토큰 갱신 중 오류 발생: ${e.message}")
                null
            }
        } else {
            Log.e("AuthInterceptor", "사용자가 로그인되지 않았습니다.")
            null
        }
    }
}
