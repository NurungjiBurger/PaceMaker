package com.maker.pacemaker.ui.screen.Component

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
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

    // 각 점수 (score1, score2, score3, score4, score5)의 평균을 계산
    val score1Sum = interviews.filterNotNull().sumOf { it.score1 }
    val score2Sum = interviews.filterNotNull().sumOf { it.score2 }
    val score3Sum = interviews.filterNotNull().sumOf { it.score3 }
    val score4Sum = interviews.filterNotNull().sumOf { it.score4 }
    val score5Sum = interviews.filterNotNull().sumOf { it.score5 }

    // 점수의 평균 계산 (질문 개수로 나누기)
    val interviewCount = interviews.filterNotNull().size
    val averageScore1 = if (interviewCount > 0) score1Sum / interviewCount else 0f
    val averageScore2 = if (interviewCount > 0) score2Sum / interviewCount else 0f
    val averageScore3 = if (interviewCount > 0) score3Sum / interviewCount else 0f
    val averageScore4 = if (interviewCount > 0) score4Sum / interviewCount else 0f
    val averageScore5 = if (interviewCount > 0) score5Sum / interviewCount else 0f

    // 평균 점수를 리스트로 묶어서 레이더 차트에 전달
    val averageScores = listOf(
        averageScore1.toFloat(),
        averageScore2.toFloat(),
        averageScore3.toFloat(),
        averageScore4.toFloat(),
        averageScore5.toFloat()
    )

    AlertDialog(
        modifier = Modifier
            .heightIn(max = height), // 다이얼로그의 최대 높이 제한
        onDismissRequest = onDismiss,
        title = {
            Text(text = "면접 결과")
        },
        text = {
            LazyColumn(
            ) {
                item {
                    // 여기에 레이더 차트를 추가
                    RadarChartView(scores = averageScores)

                    Spacer(modifier = Modifier.height(16.dp))

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


@Composable
fun RadarChartView(scores: List<Float>) {
    AndroidView(
        factory = { context ->
            RadarChart(context).apply {
                // 레이블 정의
                val labels = listOf("기술적 깊이", "문제 해결 능력", "논리적 사고력", "학습 능력", "커뮤니케이션 능력")

                // 레이더 차트 설정
                val entries = scores.mapIndexed { index, score ->
                    RadarEntry(score, index.toFloat()) // x 값을 인덱스로 설정
                }

                val dataSet = RadarDataSet(entries, "평가 점수").apply {
                    color = ColorTemplate.VORDIPLOM_COLORS[0]  // 차트 선 색상
                    fillColor = ColorTemplate.VORDIPLOM_COLORS[0]  // 채우기 색상
                    setDrawFilled(true)  // 차트 안을 색으로 채우기
                }

                // Y축 점수 숨기기
                this.yAxis.apply {
                    axisMinimum = 0f // 최소값 0
                    axisMaximum = 100f // 최대값 100
                    setDrawLabels(false) // 점수 레이블 숨기기
                }

                // X축 레이블 설정
                this.xAxis.apply {
                    valueFormatter = IndexAxisValueFormatter(labels) // 레이블 설정
                    textSize = 9f  // 레이블 텍스트 크기
                    yOffset = 5f // 레이블 위치를 그래프에서 살짝 떨어뜨림
                }

                val radarData = RadarData(dataSet)
                this.setExtraOffsets(0f, 0f, 0f, 0f) // 그래프 외부 여백 조정
                this.webLineWidth = 1f // 그래프 선 두께
                this.webLineWidthInner = 0.5f // 내부 선 두께
                this.setWebAlpha(100) // 투명도 설정
                this.data = radarData  // 데이터 설정
                this.description.isEnabled = false // 설명 비활성화
                this.invalidate()  // 뷰 갱신
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp),
    )
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
                    text = if (interview.answer.isNullOrEmpty()) "상세 내용 없음" else interview.answer,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal,
                )
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = if (interview.score1 == 0) "기술적 깊이 : 답변 길이가 너무 짧습니다." else "기술적 깊이 : ${interview.score1}"
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (interview.score2 == 0) "문제 해결 능력 : 답변 길이가 너무 짧습니다." else "문제 해결 능력 : ${interview.score2}"
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (interview.score3 == 0) "논리적 사고력 : 답변 길이가 너무 짧습니다." else "논리적 사고력 : ${interview.score3}"
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (interview.score4 == 0) "학습 능력 : 답변 길이가 너무 짧습니다." else "학습 능력 : ${interview.score4}"
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (interview.score5 == 0) "커뮤니케이션 능력 : 답변 길이가 너무 짧습니다." else "커뮤니케이션 능력 : ${interview.score5}"
                )
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
