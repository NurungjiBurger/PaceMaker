package com.maker.pacemaker.di

import dagger.Module  // 수정된 임포트 경로
import dagger.Provides  // 수정된 임포트 경로
import com.maker.pacemaker.data.model.remote.OpenAIService
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOpenAIService(): OpenAIService {
        return Retrofit.Builder()
            .baseUrl("https://api.openai.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenAIService::class.java)
    }
}