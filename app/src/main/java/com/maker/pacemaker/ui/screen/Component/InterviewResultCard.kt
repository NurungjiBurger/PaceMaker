package com.maker.pacemaker.ui.screen.Component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maker.pacemaker.data.model.remote.sendCVResponse

@Composable
fun InterviewResultCard(
    interviewResult: sendCVResponse,
    onClick: (sendCVResponse) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color(0xFFF2F2F2))
            .clickable { onClick(interviewResult) }
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = "면접 결과",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("면접 날짜: ${formatTime(interviewResult.time)}") // 시간 포맷팅
            Spacer(modifier = Modifier.height(4.dp))
            Text("면접 번호: ${interviewResult.cv_id}")
        }
    }
}