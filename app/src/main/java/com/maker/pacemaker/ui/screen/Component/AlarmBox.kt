package com.maker.pacemaker.ui.screen.Component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.maker.pacemaker.R
import com.maker.pacemaker.data.model.test.DummyMainBaseViewModel
import com.maker.pacemaker.ui.viewmodel.BaseViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmBox(
    baseViewModel: BaseViewModel,
    alarmId: Long,
    alarmType: String,
    content: String,
    dateTime: String,
    type: Boolean,
    onDismiss: () -> Unit, // 삭제 콜백 추가
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { dismissValue ->
            when (dismissValue) {
                SwipeToDismissBoxValue.EndToStart -> {
                    onDismiss() // 삭제 콜백 호출
                    false // 상태를 false로 설정하여 삭제로 간주
                }
                SwipeToDismissBoxValue.StartToEnd -> false // 삭제하지 않음
                else -> true // 스와이프가 취소된 경우
            }
        },
        positionalThreshold = { it * 0.7f } // 스와이프 감지 임계값 설정
    )

    SwipeToDismiss(
        state = dismissState,
        background = {
            // 삭제 배경
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text("삭제", color = Color.White, modifier = Modifier.padding(16.dp))
            }
        },
        dismissContent = {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(start = 10.dp, end = 10.dp, top = 0.dp, bottom = 0.dp)
                    .background(Color.Transparent)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {

                    Box(
                        modifier = Modifier
                            .background(color = Color(0xFFDFDFDF))
                            .fillMaxWidth()
                            .height(3.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp, top = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(
                                id = when {
                                    alarmType.contains("학습") -> R.drawable.alarmicon
                                    alarmType.contains("공지") -> R.drawable.noticeicon
                                    alarmType.contains("경험치") -> R.drawable.expicon
                                    alarmType.contains("친구") -> R.drawable.followicon
                                    else -> R.drawable.checkicon
                                }
                            ),
                            contentDescription = "alarm",
                            modifier = Modifier.size(20.dp)
                        )

                        Spacer(modifier = Modifier.size(3.dp))

                        Text(
                            text = alarmType,
                            modifier = Modifier.padding(start = 10.dp),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = content,
                        fontSize = 15.sp,
                        modifier = Modifier.padding(start = 20.dp)
                    )

                    Text(
                        text = formatDateTime(dateTime),
                        fontSize = 10.sp,
                        modifier = Modifier
                            .padding(start = 20.dp, top = 5.dp)
                            .background(Color.White)
                    )

                    // 마지막 아이템은 아래쪽 추가
                    Box(
                        modifier = Modifier
                            .background(color = if (type) Color(0xFFDFDFDF) else Color.Transparent)
                            .fillMaxWidth()
                            .height(3.dp)
                    )
                }
            }
        }
    )
}

@Composable
@Preview
fun PreviewAlarmBox() {
    // Dummy ViewModel을 사용한 미리보기
    val baseViewModel = DummyMainBaseViewModel()
    AlarmBox(
        baseViewModel = baseViewModel,
        alarmId = 1L,
        alarmType = "공지",
        content = "새로운 공지가 있습니다.",
        dateTime = "2024-10-24 12:00:00",
        type = false,
        onDismiss = { /* 삭제 처리 로직 */ }
    )
}

fun formatDateTime(dateTime: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val outputFormatTime = SimpleDateFormat("MM월 dd일 HH:mm", Locale.getDefault()) // 오늘의 메시지 형식
    val outputFormatDate = SimpleDateFormat("MM월 dd일", Locale.getDefault()) // 이전 메시지 형식

    // 현재 날짜와 비교하기 위해 오늘 날짜 구하기
    val currentDate = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.time

    return try {
        // dateTime을 Date 객체로 파싱
        val date = inputFormat.parse(dateTime)

        if (date != null) {
            // 오늘 날짜 이후인지 체크
            if (date >= currentDate) {
                // 오늘 날짜라면 시간까지 표시
                outputFormatTime.format(date)
            } else {
                // 오늘 이전 날짜라면 날짜만 표시
                outputFormatDate.format(date)
            }
        } else {
            // dateTime이 잘못된 경우 원본 문자열 반환
            dateTime
        }
    } catch (e: Exception) {
        // 파싱 에러가 발생하면 원본 문자열 반환
        dateTime
    }
}