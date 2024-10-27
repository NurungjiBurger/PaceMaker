package com.maker.pacemaker.ui.screen.Component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.maker.pacemaker.R
import com.maker.pacemaker.data.model.ScreenType
import com.maker.pacemaker.ui.viewmodel.BaseViewModel

@Composable
fun TopNavBar(baseViewModel: BaseViewModel) {

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(Color.Transparent) //color = Color(0xFFFAFAFA))
    ) {
        // Constraints 정의
        val (navBar) = createRefs()

        Row(
            modifier = Modifier
                .background(Color.Transparent)
                .fillMaxWidth()
                .height(60.dp)
                .padding(start = 30.dp, end = 30.dp)
                .constrainAs(navBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(id = R.drawable.usermenu),
                contentDescription = "usermenu",
                modifier = Modifier
                    .size(40.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { baseViewModel.goScreen(ScreenType.MENU) }
            )

            Image(
                painter = painterResource(id = R.drawable.alarm),
                contentDescription = "alarm",
                modifier = Modifier
                    .size(40.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { baseViewModel.goScreen(ScreenType.ALARM) }
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun TopNavBarPreview() {

//    val baseViewModel = DummyBaseViewModel()
//
//    // 모든 더미 ViewModel을 전달하여 미리보기 실행
//    TopNavBar(baseViewModel)
}