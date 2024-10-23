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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmBox(
    baseViewModel: BaseViewModel,
    alarmId: Long,
    alarmType: String,
    content: String,
    dateTime: String,
    onDismiss: () -> Unit // 삭제 콜백 추가
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { dismissValue ->
            when (dismissValue) {
                SwipeToDismissBoxValue.EndToStart -> {
                    onDismiss() // 삭제 콜백 호출
                    false // 상태를 false로 설정하여 삭제로 간주
                }
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
                    .padding(start = 5.dp, end = 10.dp)
                    .background(Color.Transparent)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .background(color = Color(0xFFDFDFDF))
                            .fillMaxWidth()
                            .height(5.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, end = 10.dp, top = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(
                                id = when {
                                    alarmType.contains("학습") -> R.drawable.alarm
                                    alarmType.contains("공지") -> R.drawable.siren
                                    alarmType.contains("경험치") -> R.drawable.trophy
                                    else -> R.drawable.siren
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
                        modifier = Modifier.padding(start = 10.dp)
                    )

                    Text(
                        text = dateTime,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(start = 10.dp, bottom = 5.dp)
                    )

                    Box(
                        modifier = Modifier
                            .background(color = Color(0xFFDFDFDF))
                            .fillMaxWidth()
                            .height(5.dp)
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
        onDismiss = { /* 삭제 처리 로직 */ }
    )
}
