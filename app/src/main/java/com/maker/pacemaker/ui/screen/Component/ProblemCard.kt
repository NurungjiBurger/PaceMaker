package com.maker.pacemaker.ui.screen.Component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.maker.pacemaker.data.model.remote.Problem
import com.maker.pacemaker.ui.viewmodel.BaseViewModel

@Composable
fun ProblemCard(
    problem: Problem,
    onClick: () -> Unit)
{

    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .height(150.dp)
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .border(1.dp, Color(0xFFD9D9D9), shape = RoundedCornerShape(8.dp))
            .clickable(
                onClick = onClick,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) // 클릭 이벤트 처리
    ) {

        // word 필드에서 answer JSON 문자열을 추출합니다.
        val wordJson = problem.word

        // Gson을 사용하여 JSON 파싱을 수행합니다.
        val gson = Gson()
        val jsonObject = gson.fromJson(wordJson, JsonObject::class.java)

        // answer 배열을 가져오고 첫 번째 답변을 추출합니다.
        val firstAnswer = jsonObject.getAsJsonArray("answer").firstOrNull()?.asString ?: "정답이 없습니다."

        Text(
            text = firstAnswer, // 문제 제목 또는 내용
            modifier = Modifier
                .padding(16.dp),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = problem.description,
            modifier = Modifier
                .padding(16.dp),
            fontSize = 15.sp,
            fontWeight = FontWeight.Normal,
            color = Color.Black
        )
    }
}