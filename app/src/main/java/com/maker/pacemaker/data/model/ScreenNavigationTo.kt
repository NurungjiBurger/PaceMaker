package com.maker.pacemaker.data.model

import androidx.navigation.NavController

data class ScreenNavigationTo(val screenType: ScreenType)

// Enum 클래스에서 Intent 정보를 관리하도록 수정
enum class ScreenType(val navigateTo: (NavController) -> Unit) {
    FINISH({ null }),

    // 부트
    BOOT({ navController -> navController.navigate("bootScreen") }),
    LOGIN({ navController -> navController.navigate("loginScreen") }),

    // 메인
    MAIN({ navController -> navController.navigate("mainScreen") }),
    TEST({ naveController -> naveController.navigate("testScreen") }),

}