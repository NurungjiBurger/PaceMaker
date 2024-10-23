package com.maker.pacemaker.ui.screen.Component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.maker.pacemaker.R
import com.maker.pacemaker.data.model.test.DummyBaseViewModel
import com.maker.pacemaker.ui.viewmodel.BaseViewModel

@Composable
fun Logo(baseViewModel: BaseViewModel) {

    // user rank

    ConstraintLayout(
        modifier = Modifier
            .size(60.dp)
            .background(Color.Transparent)
    ) {
        val (logo) = createRefs()

        Box(
            modifier = Modifier
                .size(60.dp)
                .constrainAs(logo) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
        ) {
            Image(
                painter = painterResource(id = R.drawable.alarm),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(60.dp)
            )
        }
    }
}

@Composable
@Preview
fun PreviewLogo() {

    val baseViewModel = DummyBaseViewModel()

    Logo(baseViewModel)
}