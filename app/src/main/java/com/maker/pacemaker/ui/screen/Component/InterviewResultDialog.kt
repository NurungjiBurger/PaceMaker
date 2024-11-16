package com.maker.pacemaker.ui.screen.Component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maker.pacemaker.data.model.remote.Interview
import com.maker.pacemaker.data.model.remote.sendCVResponse
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun InterviewResultDialog(
    interviewResult: sendCVResponse,
    interviews: List<Interview?>,
    width: Dp,
    height: Dp,
    onDismiss: () -> Unit
) {
    // 선택된 인터뷰를 저장할 상태
    val selectedInterview = remember { mutableStateOf<Interview?>(null) }

    AlertDialog(
        modifier = Modifier
            .width(width) // 다이얼로그의 너비 제한
            .heightIn(max = height), // 다이얼로그의 최대 높이 제한
        onDismissRequest = onDismiss,
        title = {
            Text(text = "면접 결과")
        },
        text = {
            LazyColumn(
            ) {
                item {
                    Text("면접 번호: ${interviewResult.cv_id}", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("자기소개서: ${interviewResult.cv}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("면접 시간: ${formatTime(interviewResult.time)}") // 시간 포맷팅
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("면접 질문 리스트")
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // 면접 질문 리스트가 비어있지 않으면 LazyColumn 내에 질문 항목을 나열
                if (!interviews.isNullOrEmpty()) {
                    items(interviews.size) { index ->
                        interviews[index]?.let {
                            InterviewCard(index, it) {
                                // 인터뷰 질문을 클릭하면 두 번째 다이얼로그 열기
                                selectedInterview.value = interviews[index]
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("닫기")
            }
        },
        // 다이얼로그 배경색을 하얀색으로 설정
        containerColor = Color.White
    )

    // 두 번째 다이얼로그 처리: 상세 질문
    selectedInterview.value?.let { interview ->
        InterviewDetailDialog(interview = interview) {
            selectedInterview.value = null // 상세 다이얼로그 닫기
        }
    }
}


// 면접 시간을 변환하기 위한 함수
fun formatTime(time: String): String {
    return try {
        // T를 공백으로 바꾸고 LocalDateTime으로 변환
        val dateTime = LocalDateTime.parse(time.replace("T", " "))
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss") // 원하는 형식
        dateTime.format(formatter)
    } catch (e: Exception) {
        time // 변환에 실패하면 원래의 시간을 반환
    }
}

@Composable
fun InterviewCard(index: Int, interview: Interview, onClick: () -> Unit) {
    // 인터뷰 카드 내용 표시, 클릭 시 onClick 호출
    Text(
        text = "질문 ${index + 1} : ${interview.question}",
        fontSize = 15.sp,
        modifier = Modifier
            .padding(8.dp)
            .background(Color.White)
            .clickable { onClick() })
}

@Composable
fun InterviewDetailDialog(interview: Interview, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "면접 질문 상세")
        },
        text = {
            Column {
                Text(
                    text = "질문",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "${interview.question}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "답변",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "${interview.answer}" ?: "상세 내용 없음",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal,
                )
                Spacer(modifier = Modifier.height(15.dp))
                Text(text = "기술적 깊이 : ${interview.score1}")
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "문제 해결 능력 : ${interview.score2}")
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "논리적 사고력 : ${interview.score3}")
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "학습 능력 : ${interview.score4}")
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "커뮤니케이션 능력 : ${interview.score5}")
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("닫기")
            }
        },
        // 다이얼로그 배경색을 하얀색으로 설정
        containerColor = Color.White
    )
}
