package com.maker.pacemaker.data.model.remote

import android.content.Context
import com.maker.pacemaker.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitClient {


    private lateinit var retrofit: Retrofit

    fun getRetrofitInstance(context: Context): Retrofit {
        if (!::retrofit.isInitialized) {
            // OkHttpClient에 AuthInterceptor 추가
            val client = OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor(context))
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS)
                .writeTimeout(100, TimeUnit.SECONDS)
                .build()

            retrofit = Retrofit.Builder()
                // now test server
                .baseUrl(BuildConfig.TEST_BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit
    }

    val apiService: ApiService
        get() = retrofit.create(ApiService::class.java)
}