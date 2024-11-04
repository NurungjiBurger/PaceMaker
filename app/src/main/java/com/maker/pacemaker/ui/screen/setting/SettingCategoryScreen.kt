package com.maker.pacemaker.ui.screen.setting

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.maker.pacemaker.data.model.ScreenType
import com.maker.pacemaker.ui.screen.Component.BoxCard
import com.maker.pacemaker.ui.viewmodel.setting.details.SettingCategoryScreenViewModel

@Composable
fun SettingCategoryScreen(viewModel: SettingCategoryScreenViewModel) {

    val baseViewModel = viewModel.baseViewModel
    val settingViewModel = viewModel.settingViewModel

    val settingCategories = settingViewModel.categoryList.collectAsState().value

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp // 전체 화면 높이
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp // 전체 화면 너비

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFFAFAFA))
    ) {
        val (dialog, description, contentBox, selectBox) = createRefs()

        Text(
            text = "문제를 풀 카테고리를\n선택해 보세요.",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .constrainAs(dialog) {
                    top.linkTo(parent.top, margin = 100.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        // LazyVerticalGrid를 사용하여 카테고리 리스트를 표시합니다.
        LazyVerticalGrid(
            columns = GridCells.Fixed(3), // 3개씩 가로로 배치
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .constrainAs(contentBox) {
                    top.linkTo(dialog.bottom, margin = 50.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Use items() to directly consume the settingCategories
            items(settingCategories.size) { index -> // Iterate based on the size of the list
                val category = settingCategories[index] // Get the category using the index
                BoxCard(
                    baseViewModel,
                    100.dp,
                    100.dp,
                    category, // Category name
                    20,
                    "", // Set description as needed
                    30, // Set value as needed
                    false,
                    onClick = { Log.d("TEST", "add to next") } // Handle category selection
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .constrainAs(selectBox) {
                    top.linkTo(contentBox.bottom, margin = 10.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .width(150.dp)
                    .height(50.dp)
                    .background(Color(0xFF1429A0), shape = RoundedCornerShape(50.dp))
                    .border(1.dp, Color(0xFF000000), shape = RoundedCornerShape(50.dp))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                       // viewModel.completeDailySetting(false)
                        baseViewModel.goScreen(ScreenType.MYPAGE)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "취소",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }


            Box(
                modifier = Modifier
                    .width(150.dp)
                    .height(50.dp)
                    .background(Color(0xFF1429A0), shape = RoundedCornerShape(50.dp))
                    .border(1.dp, Color(0xFF000000), shape = RoundedCornerShape(50.dp))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        //viewModel.completeDailySetting(true)
                        baseViewModel.goScreen(ScreenType.MYPAGE)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "선택완료",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}