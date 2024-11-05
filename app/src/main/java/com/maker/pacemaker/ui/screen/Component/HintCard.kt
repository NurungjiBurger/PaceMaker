package com.maker.pacemaker.ui.screen.Component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.maker.pacemaker.ui.viewmodel.BaseViewModel

@Composable
fun HintCard(
    baseViewModel: BaseViewModel,
    width: Dp,
    height: Dp,
    text: String,
    textSize: Int,
    onClick: () -> Unit
) {

    ConstraintLayout(
        modifier = Modifier
            .width(width)
            .height(height)
            .background(Color(0xFFFFCC5D).copy(alpha = 0.5f), shape = RoundedCornerShape(10.dp))
            .border(
                width = 1.dp,
                color = Color(0xFFFFCC5D),
                shape = RoundedCornerShape(10.dp)
            )
            .clickable(
                onClick = onClick,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            )
    ) {
        val (hintText) = createRefs()

        Text(
            text = text,
            fontSize = textSize.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.constrainAs(hintText) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            }
        )
    }

}