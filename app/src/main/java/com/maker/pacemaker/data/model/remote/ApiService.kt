package com.maker.pacemaker.data.model.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

     //회원가입시 서버로 전송
    @POST("/api/verifyToken") // 서버의 엔드포인트 URL에 맞춰 수정
    fun sendUserInfo(
        @Body info: infoRequest
    ): infoResponse

    // 로그인 시 서버로 전송
    @POST("users/") // 서버의 엔드포인트 URL에 맞춰 수정
    suspend fun sendIdToken(
        @Body server: loginRequest
    ): loginResponse


    // 문제 조회
    @GET("problems/{problem_id}")
    suspend fun getProblemById(@Path("problem_id") problemId: Int): Problem

    // 특정 데이터가 들어간 문제 조회
    @GET("problems/")
    suspend fun getProblemsByKeyWord(
        @Query("keyword") keyword: String
    ): List<Problem>

    // 문제 힌트 조회
    @GET("problems/{problem_id}/hints")
    suspend fun getProblemHints(
        @Path("problem_id") problemId: Int
    ): ProblemHintResponse

    // 정답 확인
    @POST("solutions/")
    suspend fun solveProblem(
        @Query("user_id") userId: Int,
        @Query("problem_id") problemId: Int,
        @Body answer: AnswerRequest
    ): AnswerResponse

    // 문제 신고
    @POST("reports/{problem_id}")
    suspend fun reportProblem(
        @Path("problem_id") problemId: Int,
        @Body report: reportRequest
    ): reportResponse

    // 문제 생성
    @POST("problems/")
    suspend fun createProblem(
        @Body problem: CreateProblemRequest
    ): CreateProblemResponse

    // 문제 댓글 달기
    @POST("problems/{problem_id}/comments")
    suspend fun addCommnet(
        @Path("problem_id") problemId: Int,
        @Body comment: CommentRequest
    ): CommentResponse

    // 문제 댓글 조회
    @GET("problems/{problem_id}/comments")
    suspend fun getComments(
        @Path("problem_id") problemId: Int
    ): List<CommentResponse>
}

data class infoRequest(
    val UID: String,
    val nickname: String
)

data class infoResponse(
    val success: Boolean
)

// idToken 서버로 전송 요청
data class loginRequest(
    val idToken: String,
    val UID: String
)

// 서버 응답 데이터 클래스
data class loginResponse(
    val success: Boolean,
    val user_id: Int
)

// 문제 조회 응답
data class Problem(
    val problem_id: Int,
    val word: String,
    val description: String,
    val level: Int,
    val category: String,
    val tried_cnt: Int,
    val creator_user_id: Int,
)

// 정답 확인 요청
data class AnswerRequest(
    val answer: String // 정답을 포함하는 요청 본문
)

// 정답 확인 응답
data class AnswerResponse(
    val user_id: Int,
    val problem_id: Int,
    val result: Boolean,
    val exp_gained: Int,
    val new_exp: Int,
    val message: String
)

// 문제 신고 요청
data class reportRequest(
    val user_id: Int,
    val reason: String
)

// 문제 신고 응답
data class reportResponse(
    val message: String,
    val report_id: Int,
    val problem_id: Int,
    val user_id: Int,
    val reason: String
)

// 문제 생성 요청
data class CreateProblemRequest(
    val creator_user_id: Int,
    val word: String,
    val description: String,
    val category_id: Int,
    val level: Int
)

// 문제 생성 응답
data class CreateProblemResponse(
    val message: String,
    val problem_id: Int,
    val status: String
)

// 문제 댓글 달기 요청
data class CommentRequest(
    val user_id: Int,
    val comment: String
)

// 문제 댓글 달기 응답
data class CommentResponse(
    val message: String,
    val comment_id: Int,
    val problem_id: Int,
    val user_id: Int,
    val comment: String
)

// 문제 힌트 조회 응답
data class ProblemHintResponse(
    val problem_id: Int,
    val hints: List<String>
)