package com.maker.pacemaker.ui.screen.Component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.maker.pacemaker.R
import com.maker.pacemaker.data.model.ActivityType
import com.maker.pacemaker.data.model.ScreenType
import com.maker.pacemaker.ui.viewmodel.BaseViewModel

@Composable
fun UpBar(baseViewModel: BaseViewModel, titleName: String, type: Boolean, activity: ActivityType, screen: ScreenType) {

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(Color.Transparent) //color = Color(0xFFFAFAFA))
    ) {
        val (navBar) = createRefs()

        Row(
            modifier = Modifier
                .background(Color.Transparent)
                .fillMaxWidth()
                .height(60.dp)
                .constrainAs(navBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start, margin = 20.dp)
                    bottom.linkTo(parent.bottom)
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.arrowleft),
                contentDescription = "usermenu",
                modifier = Modifier.size(40.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { if (type) { baseViewModel.goActivity(activity) } else { baseViewModel.goScreen(screen) } }
            )

            Spacer(modifier = Modifier.size(20.dp))

            Text(
                text = titleName,
                fontSize = 25.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 10.dp)
            )
        }
    }
}

@Composable
@Preview
fun PreviewUpBar() {

//    val baseViewModel = DummyMainBaseViewModel()
//
//    UpBar(baseViewModel, "새 소식", false, ActivityType.FINISH, ScreenType.MAIN)
}