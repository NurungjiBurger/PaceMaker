package com.maker.pacemaker.ui.screen.Component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.maker.pacemaker.data.model.test.DummyBaseViewModel
import com.maker.pacemaker.ui.viewmodel.BaseViewModel

@Composable
fun BoxCard(
    baseViewModel: BaseViewModel,
    width: Dp,
    height: Dp,
    text: String,
    subText: String,
    onClick: () -> Unit
) {

    ConstraintLayout (
        modifier = Modifier
            .width(width)
            .height(height)
            .background(Color(0xFFFAFAFA))
            .border(1.dp, Color(0xFF000000), shape = RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
    ) {
        val (titleText, subTitleText) = createRefs()

        Text(
            text = text,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .constrainAs(titleText) {
                    top.linkTo(parent.top, margin = 10.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        if (subText != "")
        {
            Text(
                text = subText,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .constrainAs(subTitleText) {
                        top.linkTo(titleText.bottom, margin = 10.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )
        }

    }
}

@Composable
@Preview
fun BoxCardPreview() {

    val baseViewModel = DummyBaseViewModel()

    BoxCard(baseViewModel, 100.dp, 100.dp, "여유롭게", "20개",
        { baseViewModel.goScreen(ScreenType.FINISH) })
}