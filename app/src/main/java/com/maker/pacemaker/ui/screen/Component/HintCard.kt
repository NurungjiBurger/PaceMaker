package com.maker.pacemaker.ui.screen.Component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
            .background(Color.White, shape = RoundedCornerShape(10.dp))
            .border(
                width = 1.dp,
                color = Color.Gray,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable(onClick = onClick)
    ) {
        val (hintText, buttonBox) = createRefs()

        Text(
            text = text,
            fontSize = textSize.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.constrainAs(hintText) {
                top.linkTo(parent.top, 15.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(buttonBox) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom, 15.dp)
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
        ){
            Text(
                text = "좋아요",
                fontSize = 10.sp,
                color = Color.Gray,
                modifier = Modifier
                    .clickable { baseViewModel.triggerToast("좋아요") }
            )

            Text(
                text = "싫어요",
                fontSize = 10.sp,
                color = Color.Gray,
                modifier = Modifier
                    .clickable { baseViewModel.triggerToast("싫어요") }
            )
        }
    }

}