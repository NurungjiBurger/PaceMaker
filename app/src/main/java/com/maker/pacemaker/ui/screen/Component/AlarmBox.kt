package com.maker.pacemaker.ui.screen.Component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
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
    alarmType: String,
    content: String,
    dateTime: String,
    onDismiss: () -> Unit // 삭제 콜백 추가
) {
    // Dismiss 상태 초기화
    val dismissState = rememberSwipeToDismissBoxState()

    SwipeToDismiss(
        state = dismissState,
        background = {
            // 삭제 배경
            Box(
                modifier = Modifier.fillMaxWidth().background(Color.Red),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text("삭제", color = Color.White)
            }
        },
        dismissContent = {
            // 기존 AlarmBox 코드
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(start = 5.dp, end = 10.dp)
                    .background(Color.Transparent)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
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
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Image(
                            painter = painterResource(
                                id = when (alarmType) {
                                    alarmType.contains("학습").toString() -> R.drawable.alarm
                                    alarmType.contains("공지").toString() -> R.drawable.siren
                                    alarmType.contains("경험치").toString() -> R.drawable.trophy
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

    // Swipe 상태에 따라 삭제 콜백 호출
    if (dismissState.dismissDirection != SwipeToDismissBoxValue.EndToStart) {
        onDismiss() // onDismiss를 호출합니다.
    }
}



@Composable
@Preview
fun PreviewAlarmBox() {

    //val baseViewModel = DummyMainBaseViewModel()

    //AlarmBox(baseViewModel, "알람", "알람 내용", "11월 28일")
}