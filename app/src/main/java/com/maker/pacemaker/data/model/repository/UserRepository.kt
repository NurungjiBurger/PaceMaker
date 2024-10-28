package com.maker.pacemaker.data.model.repository

import com.maker.pacemaker.data.model.remote.AnswerRequest
import com.maker.pacemaker.data.model.remote.AnswerResponse
import com.maker.pacemaker.data.model.remote.ApiService
import com.maker.pacemaker.data.model.remote.CommentRequest
import com.maker.pacemaker.data.model.remote.CommentResponse
import com.maker.pacemaker.data.model.remote.CreateProblemRequest
import com.maker.pacemaker.data.model.remote.CreateProblemResponse
import com.maker.pacemaker.data.model.remote.Problem
import com.maker.pacemaker.data.model.remote.ProblemHintResponse
import com.maker.pacemaker.data.model.remote.ProblemList
import com.maker.pacemaker.data.model.remote.reportRequest
import com.maker.pacemaker.data.model.remote.reportResponse


class UserRepository(private val apiService: ApiService) {

    // 문제 조회
    suspend fun getProblemById(problemId: Int) : Problem {
        return apiService.getProblemById(problemId)
    }

    // 특정 데이터가 들어간 문제 조회
    suspend fun getProblemsByKeyWord(keyword: String) : ProblemList {
        return apiService.getProblemsByKeyWord(keyword)
    }

    // 문제 힌트 조회
    suspend fun getProblemHints(problemId: Int) : ProblemHintResponse {
        return apiService.getProblemHints(problemId)
    }

    // 정답 확인
    suspend fun solveProblem(userId: Int, problemId: Int, answer: AnswerRequest) : AnswerResponse {
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

}