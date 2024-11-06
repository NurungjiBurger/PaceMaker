package com.maker.pacemaker.ui.screen.Component

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.maker.pacemaker.R
import com.maker.pacemaker.data.model.User
import com.maker.pacemaker.data.model.remote.SearchUser
import com.maker.pacemaker.ui.viewmodel.BaseViewModel

@Composable
fun UserCard(
    baseViewModel: BaseViewModel,
    width: Dp,
    height: Dp,
    user: SearchUser,
    onClick: () -> Unit,
    followToggle: (() -> Unit)? = null
) {

    val context = LocalContext.current
    // 레벨 값을 1에서 6 사이로 보정
    val correctedLevel = when {
        user.level < 1 -> 1
        user.level > 6 -> 6
        else -> user.level
    }

    val resourceId = context.resources.getIdentifier(
        "level_$correctedLevel", "drawable", context.packageName
    )

    ConstraintLayout(
        modifier = Modifier
            .width(width)
            .height(height)
            .background(if (followToggle != null) Color.White else Color(0xFFFAFAFA))
            .border(
                width = 1.dp,
                color = if (followToggle != null) Color.Transparent else Color(0xFF000000),
                shape = RoundedCornerShape(10.dp)
            )
            .clickable(
                onClick = onClick,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            )
    ) {

        val (infoBox, image) = createRefs()

        Column(
            modifier = Modifier
                .constrainAs(infoBox) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(image.start)
                    bottom.linkTo(parent.bottom)
                }
        ){
            Text(
                text = user.nickname,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(10.dp)
            )
//"${user.followers} followers",
            Text(
                text = "followers",
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Gray,
                modifier = Modifier
                    .padding(10.dp)
            )
        }

        Image(
            painter = painterResource(id = resourceId),
            contentDescription = null,
            modifier = Modifier
                .width(width/2)
                .height(height)
                .padding(10.dp)
                .constrainAs(image) {
                    top.linkTo(parent.top)
                    start.linkTo(infoBox.end)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
        )
    }


}