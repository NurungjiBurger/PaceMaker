package com.maker.pacemaker.ui.screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun MainScreen() {
    Text(text = "Welcome to Main Screen!")
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen()
}