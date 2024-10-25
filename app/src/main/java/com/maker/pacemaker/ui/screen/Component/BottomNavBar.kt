package com.maker.pacemaker.ui.screen.Component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.maker.pacemaker.R
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
                    .clickable {
                        baseViewModel.floatingToastMessage("랭킹페이지입니다.")
                    }
            )

            // 문제 검색
            Image(
                painter = painterResource(id = R.drawable.search),
                contentDescription = "Search",
                modifier = Modifier
                    .size(40.dp)
                    .clickable {
                        baseViewModel.floatingToastMessage("문제검색페이지입니다.")
                    }
            )

            // 실험실
            Image(
                painter = painterResource(id = R.drawable.lab),
                contentDescription = "Lab",
                modifier = Modifier
                    .size(40.dp)
                    .clickable {
                        baseViewModel.floatingToastMessage("실험실페이지입니다.")
                    }
            )

            // etc...
            Image(
                painter = painterResource(id = R.drawable.setting),
                contentDescription = "Etc",
                modifier = Modifier
                    .size(40.dp)
                    .clickable {
                        baseViewModel.floatingToastMessage("기타페이지입니다.")
                    }
            )

        }

    }
}

@Preview(showBackground = true)
@Composable
fun BottomNavBarPreview() {

//    val baseViewModel = DummyBaseViewModel()
//
//    // 모든 더미 ViewModel을 전달하여 미리보기 실행
//    BottomNavBar(baseViewModel)
}