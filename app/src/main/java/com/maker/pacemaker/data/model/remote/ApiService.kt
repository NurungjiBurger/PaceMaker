package com.maker.pacemaker.data.model.remote

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.logging.Level

interface ApiService {

     //회원가입시 서버로 전송, create_user
     @POST("users/")
     suspend fun createUser(
         @Query("nickname") nickname: String
     ): userResponse

    // 로그인 시 서버로 전송
    @POST("users/") // 서버의 엔드포인트 URL에 맞춰 수정
    suspend fun sendIdToken(
        @Body server: loginRequest
    ): loginResponse

    // 문제 조회
    @GET("problems/{problem_id}")
    suspend fun getProblemById(@Path("problem_id") problemId: Int): Problem

    // 데일리 문제 조회
    @GET("problems/daily")
    suspend fun getDailyProblem(
    ): List<Problem>

    // 특정 데이터가 들어간 문제 조회
    @GET("problems/")
    suspend fun getProblemsByKeyWord(
        @Query("keyword") keyword: String
    ): List<Problem>

    // 문제 힌트 조회
    @GET("problems/{problem_id}/hints")
    suspend fun getProblemHints(
        @Path("problem_id") problemId: Int
    ): List<ProblemHint>

    // 정답 확인
    @POST("solutions/")
    suspend fun solveProblem(
        @Query("user_id") userId: String,
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

    // 유저 카테고리 설정
    @PATCH("users/me/categories")
    suspend fun updateCategories(
        @Body preferred_categories: updateCategoriesRequest
    ): updateCategoriesResponse

    // 유저 레벨 설정
    @PATCH("users/me/level")
    suspend fun updateLevel(
        @Body levelResult: LevelRequest
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

    //////////////////////////////////////////////////////////

    // fcm 토큰 전송
    @POST("fcm/")
    suspend fun sendFcmToken(
        @Body sendFcmTokenRequest: sendFcmToken
    ): sendFcmToken

    // fcm 토큰 조회
    @GET("fcm/{user_id}")
    suspend fun getFcmToken(
        @Path("user_id") user_id: String
    ): getFcmTokenResponse

    // fcm 토큰 삭제
    @DELETE("fcm/{fcm_token}")
    suspend fun deleteFcmToken(
        @Path("fcm_token") fcm_token: String
    )

    //////////////////////////////////////////////////////

    @GET("categories/")
    suspend fun getCategories(
    ): getCategoriesResponse


    //////////////////////////////////////////////////////

    // 레벨 테스트 문제 조회
    @GET("/level-test/{level}")
    suspend fun getLevelTestProblemsByLevel(
        @Path("level") level: Int
    ): List<Problem>

    //싸맨틀 게임
    @POST("ssamantle/attempt")
    suspend fun checkWordSimilarity(
        @Body request: SimilarityWordRequest
    ): SimilarityWordResponse

    //싸맨틀 랭킹 조회
    @GET("ssamantle/solved_users")
    suspend fun getSolvedUsers(): List<SolvedUser>

    //////////////////////////////////////////////////////////////

    // 질문번호로 질문 받아오기
    @GET("interviews/{interview_id}")
    suspend fun getInterviewById(
        @Path("interview_id") interviewId: Int
    ): Interview

    // 이력서 번호로 질문 리스트 받아오기
    @GET("interviews/cv/{cv_id}")
    suspend fun getInterviewsByCVId(
        @Path("cv_id") cvId: Int
    ): List<Interview>

    // 답변 전송하기
    @POST("interviews/{interview_id}/answer")
    suspend fun sendInterviewAnswer(
        @Path("interview_id") interviewId: Int,
        @Body answer: InterviewAnswerRequest
    ): Interview

    // cv 보내기
    @POST("cvs/")
    suspend fun sendCV(
        @Body sendCVRequest: CV
    ): sendCVResponse

    // cv id로 cv 가져오기
    @GET("cvs/{cv_id}")
    suspend fun getCVById(
        @Path("cv_id") cvId: Int
    ): CV

    // cv id로 준비되었는지 확인
    @GET("cvs/{cv_id}/ready")
    suspend fun checkCVReady(
        @Path("cv_id") cvId: Int
    ): Boolean

}

data class InterviewAnswerRequest(
    val answer: String
)

// 질문
data class Interview(
    val interview_id: Int,
    val cv_id: Int,
    val question: String,
    var answer: String,
    val time: String,
    val score1: Int,
    val score2: Int,
    val score3: Int,
    val score4: Int,
    val score5: Int
)

// CV 전송 요청
data class CV(
    val cv_text: String
)

// cv 전송 응답
data class sendCVResponse(
    val cv_id: Int,
    val uid: String,
    val cv: String,
    val time: String
)

// 선호 카테고리 수정 요청
data class updateCategoriesRequest(
    val preferred_categories: List<Int>
)

// 선호 카테고리 수정 응답
data class updateCategoriesResponse(
    val message: String,
    val uid: String,
    val preferred_categories: List<Int>
)

// 카테고리 조회 응답
data class getCategoriesResponse(
    val categories: List<Category>
)

data class Category(
    val category_id: Int,
    val name: String
)

///////////////////////////////////////////////

data class getFcmTokenResponse(
    val tokens: List<FCMToken>
)

data class FCMToken(
    val id: Int,
    val user_id: String,
    val fcm_token: String,
    val device_type: String
)

data class sendFcmToken(
    val user_id: String,
    val fcm_token: String,
    val device_type: String
)

/////////////////////////////////////////////////////////////

data class userRequest(
    val nickname: String
)

data class userResponse(
    val uid: String,
    val nickname: String,
    val exp: Int,
    val level: Int,
    val daily_cnt: Int,
    val message: String
)

data class User(
    val uid: String,
    val nickname: String,
    val exp: Int,
    val level: Int,
    val daily_cnt: Int,
    val preferred_categories: List<Int>,
    val followers_count: Int
)

// idToken 서버로 전송 요청
data class loginRequest(
    val idToken: String,
    val UID: String
)

// 서버 응답 데이터 클래스
data class loginResponse(
    val success: Boolean,
    val user_id: String // 유저 인덱스
)

    
// 유저 검색
data class SearchUserRequest(
    val keyword: String
)

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

// 유저 레벨 설정 요청
data class LevelRequest(
    val levelResult: Int
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
    val creator_user_id: String,
)

// 정답 확인 요청
data class AnswerRequest(
    val answer: String // 정답을 포함하는 요청 본문
)

// 정답 확인 응답
data class AnswerResponse(
    val user_id: String,
    val problem_id: Int,
    val result: Boolean,
    val exp_gained: Int,
    val new_exp: Int,
    val message: String
)

// 문제 신고 요청
data class reportRequest(
    val user_id: String,
    val reason: String
)

// 문제 신고 응답
data class reportResponse(
    val message: String,
    val report_id: Int,
    val problem_id: Int,
    val user_id: String,
    val reason: String
)

// 문제 생성 요청
data class CreateProblemRequest(
    val creator_user_id: String,
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
    val user_id: String,
    val comment: String
)

// 문제 댓글 달기 응답
data class CommentResponse(
    val message: String,
    val comment_id: Int,
    val problem_id: Int,
    val user_id: String,
    val comment: String
)

// 문제 힌트 조회 응답
data class ProblemHint(
    val problem_id: Int,
    val hint: String
)

data class SimilarityWord(
    val word: String,
    val similarity: Float,
    val similarityRank: Int,
)

//싸맨틀
// 유사도 요청 데이터 클래스
data class SimilarityWordRequest(
    val word: String
)

// 유사도 응답 데이터 클래스
data class SimilarityWordResponse(
    val try_cnt: Int,
    val similarity: Float,
    val ranking: Int
)

//싸맨틀 랭킹
data class SolvedUser(
    val uid: String,
    val nickname: String,
    val try_cnt: Int
)

