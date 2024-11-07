package com.maker.pacemaker.data.model.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface OpenAIService {
    @Headers("Authorization: Bearer YOUR_API_KEY", "Content-Type: application/json")
    @POST("v1/completions")
    suspend fun getQuestions(@Body request: OpenAIRequest): Response<OpenAIResponse>
}

data class OpenAIRequest(
    val model: String = "text-davinci-003",
    val prompt: String,
    val max_tokens: Int = 100,
    val n: Int = 3,
    val stop: List<String>? = null
)

data class OpenAIResponse(
    val choices: List<Choice>
)

data class Choice(
    val text: String
)