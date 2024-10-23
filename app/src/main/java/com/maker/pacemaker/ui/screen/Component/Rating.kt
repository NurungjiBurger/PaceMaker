package com.maker.pacemaker.ui.screen.Component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.maker.pacemaker.R
import com.maker.pacemaker.ui.viewmodel.BaseViewModel

@Composable
fun Rating(baseViewModel: BaseViewModel) {

    val userName by baseViewModel.userName.collectAsState()

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(Color.Transparent)
    ) {

        val (ranking) = createRefs()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(start = 20.dp, end = 20.dp, top = 5.dp, bottom = 5.dp)
                .constrainAs(ranking) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                },
                verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .background(color = Color(0xFFDFDFDF))
                    .fillMaxWidth()
                    .height(5.dp)
            )

            Spacer(modifier = Modifier.height(5.dp))

            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
            ) {
                val (rating, percent) = createRefs()

                Text(
                    text = "rating",
                    fontSize = 15.sp,
                    modifier = Modifier
                        .constrainAs(rating) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                            bottom.linkTo(percent.top)
                        }
                )

                Text(
                    text = "상위${userName}%",
                    fontSize = 15.sp,
                    modifier = Modifier
                        .constrainAs(percent) {
                            top.linkTo(rating.bottom)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        }
                )
            }

            Spacer(modifier = Modifier.height(5.dp))

            Box(
                modifier = Modifier
                    .background(color = Color(0xFFDFDFDF))
                    .fillMaxWidth()
                    .height(5.dp)
            )
        }
    }
}

@Composable
@Preview
fun PreviewRating() {

    val baseViewModel = BaseViewModel()

    Rating(baseViewModel)
}