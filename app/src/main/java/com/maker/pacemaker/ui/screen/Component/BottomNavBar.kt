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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.maker.pacemaker.ui.viewmodel.main.MainBaseViewModel
import com.maker.pacemaker.ui.viewmodel.main.details.MainScreenViewModel

@Composable
fun BottomNavBar(baseViewModel: BaseViewModel) {


    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent) //color = Color(0xFFFAFAFA))
    ) {
        // Constraints 정의
        val (navBar) = createRefs()

        Row(
            modifier = Modifier
                .background(color = Color(0xFFD9D9D9).copy(alpha = 0.5f))
                .fillMaxWidth()
                .height(60.dp)
                .padding(start = 30.dp, end = 30.dp)
                .constrainAs(navBar) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 랭킹
            Image(
                painter = painterResource(id = R.drawable.trophy),
                contentDescription = "Ranking",
                modifier = Modifier
                    .size(40.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { baseViewModel.goScreen(ScreenType.RANKING) }
            )

            // 문제 검색
            Image(
                painter = painterResource(id = R.drawable.search),
                contentDescription = "Search",
                modifier = Modifier
                    .size(40.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { baseViewModel.goScreen(ScreenType.PROBLEMSEARCH) }
            )

            // 실험실
            Image(
                painter = painterResource(id = R.drawable.lab),
                contentDescription = "Lab",
                modifier = Modifier
                    .size(40.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { baseViewModel.goScreen(ScreenType.LAB) }
            )

            // etc...
            Image(
                painter = painterResource(id = R.drawable.setting),
                contentDescription = "Etc",
                modifier = Modifier
                    .size(40.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { baseViewModel.goScreen(ScreenType.MENU) }
            )

        }

    }
}
