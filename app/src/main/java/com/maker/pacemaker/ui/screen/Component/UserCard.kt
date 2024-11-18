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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.font.FontStyle
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
    index: Int,
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


    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable(
                onClick = onClick,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            )
    ) {
        Text(
            text = "${index}",
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold, // 최대한 굵게 설정
            fontStyle = FontStyle.Italic,
            color = Color.Black,
            modifier = Modifier
                .padding(start = 15.dp, top = 8.dp)
        )

        Text(
            text = user.nickname,
            fontSize = 18.sp,
            maxLines = 1,
            softWrap = true,
            color = Color.Black,
        )

        Text(
            text = "${user.exp}xp",
            fontSize = 18.sp,
            color = Color.Black,
        )

        Image(
            painter = painterResource(id = resourceId),
            contentDescription = null,
            modifier = Modifier
                .size(50.dp)
                .padding(end = 15.dp)
        )
    }
}