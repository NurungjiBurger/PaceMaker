package com.maker.pacemaker.ui.screen.Component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.maker.pacemaker.ui.viewmodel.BaseViewModel
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.maker.pacemaker.R

@Composable
fun NavCard(
    baseViewModel: BaseViewModel,
    title: String,
    contentText: String,
    onClick: () -> Unit = { }
) {

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp // 전체 화면 높이
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp // 전체 화면 너비
    val boxHeight = screenHeight / 6
    val boxWidth = screenWidth - 20.dp

    ConstraintLayout(
        modifier = Modifier
            .width(boxWidth)
            .height(boxHeight)
            .padding(10.dp)
            .background(color = Color(0xFFFAFAFA), shape = RoundedCornerShape(15.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() }
    ) {
        val (titleText, content, nav) = createRefs()

        Text(
            text = title,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .constrainAs(titleText) {
                    start.linkTo(parent.start, margin = 20.dp)
                    top.linkTo(parent.top, margin = 20.dp)
                }

        )

        Text(
            text = contentText,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1429A0),
            modifier = Modifier
                .constrainAs(content) {
                    start.linkTo(titleText.start, margin = 20.dp)
                    top.linkTo(titleText.bottom, margin = 10.dp)
                }
        )

        Image(
            painter = painterResource(id = R.drawable.navigaterighticon),
            contentDescription = "Nav",
            modifier = Modifier
                .size(50.dp)
                .constrainAs(nav) {
                    end.linkTo(parent.end, margin = 10.dp)
                    top.linkTo(parent.top, margin = 40.dp)
                }
        )

    }

}