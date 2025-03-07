package com.maker.pacemaker.ui.screen.Component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.maker.pacemaker.data.model.ActivityType
import com.maker.pacemaker.data.model.ScreenType
import com.maker.pacemaker.ui.viewmodel.BaseViewModel

@Composable
fun BoxCard(
    baseViewModel: BaseViewModel,
    width: Dp,
    height: Dp,
    text: String,
    textSize: Int,
    subText: String,
    subTextSize: Int,
    isSelect: Boolean,
    onClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .width(width)
            .height(height)
            .background(Color(0xFFFAFAFA), RoundedCornerShape(10.dp))
            .border(
                width = if (isSelect) 3.dp else 1.dp, // 조건에 따라 테두리 두께 설정
                color = if (isSelect) Color(0xFF1429A0) else Color(0xFF000000), // 조건에 따라 테두리 색상 설정
                shape = RoundedCornerShape(10.dp) // 테두리 모양 설정
            )
            .clickable(
                onClick = onClick,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = text,
            fontSize = textSize.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier

        )

        if (subText != "")
        {
            Text(
                text = subText,
                fontSize = subTextSize.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier

            )
        }

    }
}
