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
import androidx.compose.runtime.getValue
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
import com.maker.pacemaker.ui.viewmodel.setting.details.SettingCategoryScreenViewModel

@Composable
fun SettingCategoryScreen(viewModel: SettingCategoryScreenViewModel) {

    val baseViewModel = viewModel.baseViewModel
    val settingViewModel = viewModel.settingViewModel

    val settingCategories by viewModel.settingCategories.collectAsState()
    val selectedCategories by viewModel.selectedCategories.collectAsState()

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp // 전체 화면 높이
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp // 전체 화면 너비

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFDFE9FE))
    ) {
        val (dialog, description, contentBox, selectBox) = createRefs()

        Text(
            text = "문제를 풀 카테고리를\n선택해 보세요",
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

        // LazyVerticalGrid를 사용하여 카테고리 리스트를 표시
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .constrainAs(contentBox) {
                    top.linkTo(dialog.bottom, margin = 50.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            items(settingCategories.size) { index ->
                val category = settingCategories[index]
                val isSelected = selectedCategories.contains(index)

                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp)
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .border(2.dp, if (isSelected) Color(0xFF1429A0) else Color(0xFFD9D9D9) , RoundedCornerShape(10.dp))
                        .clickable {
                            viewModel.toggleCategorySelection(category.category_id)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = category.name,
                        color = if (isSelected) Color(0xFF1429A0) else Color(0xFFD9D9D9),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
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
                    .clickable {
                        // 취소 클릭 시 아무 것도 저장하지 않음
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
                    .clickable {
                        // 선택된 카테고리 서버에 저장
                        viewModel.completeSelection()
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