package com.maker.pacemaker.data.model.remote

import com.maker.pacemaker.BuildConfig
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface ClovaVoiceService {

    @POST("tts")
    @Headers(
        "Content-Type: application/x-www-form-urlencoded",
        "X-NCP-APIGW-API-KEY-ID: ${BuildConfig.CLOVA_API_CLIENT_ID}",
        "X-NCP-APIGW-API-KEY: ${BuildConfig.CLOVA_API_CLIENT_SECRET}"
    )
    @FormUrlEncoded
    fun synthesize(
        @Field("speaker") speaker: String, // 목소리 (이름 선택)
        @Field("speed") speed: Int,        // 속도 (-로 갈수록 느려짐)
        @Field("volume") volume: Int,      // 소리 크기 (클수록 커짐)
        @Field("pitch") pitch: Int,        // 음역대 (-로 갈수록 저음)
        @Field("emotion-strength") emotion: Int, // 감정 표현 강도
        @Field("alpha") alpha: Int,        // 음색 (높을수록 하이톤)
        @Field("end-pitch") endPitch: Int, // 끝음 처리 (-로 내림, +로 올림)
        @Field("text") text: String        // 변환할 텍스트
    ): Call<ResponseBody>

    // STT 서비스 호출
    @POST("stt")
    @Headers(
        "Content-Type: application/octet-stream",
        "X-NCP-APIGW-API-KEY-ID: ${BuildConfig.CLOVA_API_CLIENT_ID}",
        "X-NCP-APIGW-API-KEY: ${BuildConfig.CLOVA_API_CLIENT_SECRET}",
        "X-Language: ko-KR"
    )
    fun recognizeSpeech(@Body audioData: AudioData): Call<ResponseBody>
}

data class AudioData(
    val audioFile: ByteArray // 음성 파일의 바이트 배열
)