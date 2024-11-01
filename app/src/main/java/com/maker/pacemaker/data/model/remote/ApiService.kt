package com.maker.pacemaker.data.model.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.logging.Level

interface ApiService {

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

    ///////////////////////////////////////////////////

    // 내 정보 가져오기
    @GET("users/me")
    suspend fun getMyUserInfo(

    ): User

    // 데일리 문제 수 설정
    @PATCH("users/me/daily")
    suspend fun updateDailyCnt(
        @Body request: DailyCntRequest
    ): DailyCntResponse

    // 유저 레벨 설정
    @PATCH("users/me/level")
    suspend fun updateLevel(
        @Body levelResult: Int
    ): LevelResponse

    // 유저 닉네임 설정
    @PATCH("users/me/nickname")
    suspend fun updateNickname(
        @Body nickname: String
    ): NicknameResponse

    // 유저 검색
    @GET("users/")
    suspend fun searchUser(
        @Query("keyword") keyword: String
    ): SearchUserResponse

    // 유저 uid로 검색
    @GET("users/{uid}")
    suspend fun searchUserByUid(
        @Path("uid") uid: String
    ): User
}


// 유저 정보
data class User(
    val uid: String,
    val nickname: String,
    val exp: Int,
    val level: Int,
    val daily_cnt: Int,
    val preferred_categories: List<String>,
    val followers_count: Int
)

// 유저 검색
data class SearchUserResponse(
    val users: List<SearchUser>
)

// 검색된 유저 정보
data class SearchUser(
    val uid: String,
    val nickname: String,
    val level: Int,
    val exp: Int
)

// 데일리 문제 설정 요청
data class DailyCntRequest(
    val daily_cnt: Int
)

// 데일리 문제 수 설정 응답
data class DailyCntResponse(
    val message: String,
    val uid: String,
    val daily_cnt: Int,
    val level: Int,
    val exp: Int
)

// 유저 레벨 설정 응답
data class LevelResponse(
    val message: String,
    val user_id: String,
    val new_level: Int
)

// 유저 닉네임 설정 응답
data class NicknameResponse(
    val message: String,
    val uid: String,
    val nickname: String
)

///////////////////////////////////////////////

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