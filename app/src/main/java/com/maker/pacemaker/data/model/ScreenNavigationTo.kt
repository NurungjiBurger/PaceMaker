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
    MENU({ navController -> navController.navigate("menuScreen") }),
    ALARM({ navController -> navController.navigate("alarmScreen") }),

    // 세팅
    MYPAGE({ navController -> navController.navigate("mypageScreen") }),
    DAILY({ navController -> navController.navigate("dailyScreen") }),
    RATIO({ navController -> navController.navigate("ratioScreen") }),
    CATEGORY({ navController -> navController.navigate("categoryScreen") }),
    LEVELTEST({ navController -> navController.navigate("levelTestScreen") }),

}