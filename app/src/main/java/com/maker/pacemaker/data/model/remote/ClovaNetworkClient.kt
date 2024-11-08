package com.maker.pacemaker.data.model.remote



import android.content.Context
import com.maker.pacemaker.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ClovaNetworkClient {

    private var retrofit: Retrofit? = null

    fun getRetrofitClient(context: Context): Retrofit {
        if (retrofit == null) {
            // 통신 로그 확인할 때 필요한 코드
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            // 네트워크 연결 관련 코드
            val httpClient = OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .addInterceptor(loggingInterceptor)
                .build()

            // Retrofit 인스턴스 생성
            retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.CLOVA_API_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }
}