package com.maker.pacemaker.data.model.repository

import android.util.Log
import com.maker.pacemaker.data.model.remote.AnswerRequest
import com.maker.pacemaker.data.model.remote.AnswerResponse
import com.maker.pacemaker.data.model.remote.ApiService
import com.maker.pacemaker.data.model.remote.CommentRequest
import com.maker.pacemaker.data.model.remote.CommentResponse
import com.maker.pacemaker.data.model.remote.CreateProblemRequest
import com.maker.pacemaker.data.model.remote.CreateProblemResponse
import com.maker.pacemaker.data.model.remote.DailyCntRequest
import com.maker.pacemaker.data.model.remote.DailyCntResponse
import com.maker.pacemaker.data.model.remote.LevelRequest
import com.maker.pacemaker.data.model.remote.LevelResponse
import com.maker.pacemaker.data.model.remote.NicknameResponse
import com.maker.pacemaker.data.model.remote.Problem
import com.maker.pacemaker.data.model.remote.ProblemHint
import com.maker.pacemaker.data.model.remote.loginRequest
import com.maker.pacemaker.data.model.remote.loginResponse
//import com.maker.pacemaker.data.model.remote.ServerRequest
//import com.maker.pacemaker.data.model.remote.ServerResponse
import com.maker.pacemaker.data.model.remote.SearchUserResponse
import com.maker.pacemaker.data.model.remote.SimilarityWordRequest
import com.maker.pacemaker.data.model.remote.SimilarityWordResponse
import com.maker.pacemaker.data.model.remote.SolvedUser
import com.maker.pacemaker.data.model.remote.User
import com.maker.pacemaker.data.model.remote.getCategoriesResponse
import com.maker.pacemaker.data.model.remote.getFcmTokenResponse
import com.maker.pacemaker.data.model.remote.reportRequest
import com.maker.pacemaker.data.model.remote.reportResponse
import com.maker.pacemaker.data.model.remote.sendFcmToken
import com.maker.pacemaker.data.model.remote.updateCategoriesRequest
import com.maker.pacemaker.data.model.remote.updateCategoriesResponse
import com.maker.pacemaker.data.model.remote.userRequest
import com.maker.pacemaker.data.model.remote.userResponse


class UserRepository(private val apiService: ApiService) {

    suspend fun createUser(nickname: String): userResponse {
        return apiService.createUser(nickname)
    }
    // idToken 서버로 전송
//    suspend fun sendIdToken(usertoken : loginRequest): loginResponse {
//        return apiService.sendIdToken(usertoken)
//    }

    // 문제 조회
    suspend fun getProblemById(problemId: Int) : Problem {
        return apiService.getProblemById(problemId)
    }

    // 데일리 문제 조회
    suspend fun getDailyProblem() : List<Problem> {
        return apiService.getDailyProblem()
    }

    // 특정 데이터가 들어간 문제 조회
    suspend fun getProblemsByKeyWord(keyword: String) : List<Problem> {
        return apiService.getProblemsByKeyWord(keyword)
    }

    // 문제 힌트 조회
    suspend fun getProblemHints(problemId: Int) : List<ProblemHint> {
        return apiService.getProblemHints(problemId)
    }

    // 정답 확인
    suspend fun solveProblem(userId: String, problemId: Int, answer: AnswerRequest) : AnswerResponse {
        return apiService.solveProblem(userId, problemId, answer)
    }

    // 문제 신고
    suspend fun reportProblem(problemId: Int, report: reportRequest) : reportResponse {
        return apiService.reportProblem(problemId, report)
    }

    // 문제 생성
    suspend fun createProblem(problem: CreateProblemRequest) : CreateProblemResponse {
        return apiService.createProblem(problem)
    }

    // 문제 댓글 달기
    suspend fun addCommnet(problemId: Int, comment: CommentRequest) : CommentResponse {
        return apiService.addCommnet(problemId, comment)
    }

    // 문제 댓글 조회
    suspend fun getComments(problemId: Int) : List<CommentResponse> {
        return apiService.getComments(problemId)
    }

    //////////////////////////////////////////////////////////////

    // 내 정보 조회
    suspend fun getMyUserInfo() : User {
        return apiService.getMyUserInfo()
    }

    // 데일리 문제 갯수 설정
    suspend fun updateDailyCnt(request: DailyCntRequest): DailyCntResponse {
        return apiService.updateDailyCnt(request)
    }

    // 내 유저 레벨 설정
    suspend fun updateLevel(request: LevelRequest): LevelResponse {
        return apiService.updateLevel(request)
    }

    // 유저 카테고리 설정
    suspend fun updateCategories(request: updateCategoriesRequest): updateCategoriesResponse {
        return apiService.updateCategories(request)
    }

    // 유저 닉네임 설정
    suspend fun updateNickname(nickname: String): NicknameResponse {
        return apiService.updateNickname(nickname)
    }

    // 유저 검색
    suspend fun searchUser(nickname: String): SearchUserResponse {
        return apiService.searchUser(nickname)
    }

    // 유저 UID로 검색
    suspend fun searchUserByUid(uid: String): User {
        return apiService.searchUserByUid(uid)
    }

    /////////////////////////////////////////////////////////////

    // fcm token 전송
    suspend fun sendFcmToken(request: sendFcmToken): sendFcmToken {
        return apiService.sendFcmToken(request)
    }

    // fcm token 조회
    suspend fun getFcmToken(user_id: String): getFcmTokenResponse  {
        return apiService.getFcmToken(user_id)
    }

    // fcm token 삭제
    suspend fun deleteFcmToken(fcmToken: String)  {
        return apiService.deleteFcmToken(fcmToken)
    }

    ///////////////////////////////////////////////////////////////////


    // 카테고리 조회
    suspend fun getCategories(): getCategoriesResponse {
        return apiService.getCategories()
    }

    //////////////////////////////////////////////////////////////////////

    // 레벨 테스트 문제 조회
    suspend fun getLevelTestProblemsByLevel(level: Int): List<Problem> {
        return apiService.getLevelTestProblemsByLevel(level)
    }

    //////////////////////////////////////////////////////////////////////
    //싸맨틀 게임
    suspend fun checkWordSimilarity(request: SimilarityWordRequest): SimilarityWordResponse {
        return apiService.checkWordSimilarity(request)
    }

    // 싸맨틀 랭킹 조회
    suspend fun getSolvedUsers(): List<SolvedUser> {
        return apiService.getSolvedUsers()
    }


}