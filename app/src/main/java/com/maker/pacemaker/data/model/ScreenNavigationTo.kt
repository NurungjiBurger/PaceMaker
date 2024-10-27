package com.maker.pacemaker.data.model

import androidx.navigation.NavController

data class ScreenNavigationTo(val screenType: ScreenType)

// Enum 클래스에서 Intent 정보를 관리하도록 수정
enum class ScreenType(val navigateTo: (NavController) -> Unit) {
    FINISH({ null }),

    // 부트
    BOOT({ navController -> navController.navigate("bootScreen") }),
    ENTRY({ navController -> navController.navigate("entryScreen") }),

    // 회원가입
    SIGNUP({ navController -> navController.navigate("authScreen") }),
    LOAD({ navController -> navController.navigate("loadScreen") }),

    // 메인
    MAIN({ navController -> navController.navigate("mainScreen") }),
    MENU({ navController -> navController.navigate("menuScreen") }),
    ALARM({ navController -> navController.navigate("alarmScreen") }),
    MYPAGE({ navController -> navController.navigate("mypageScreen") }),

    // 로그인
    SIGNIN({ navController -> navController.navigate("signinScreen") }),
    LOADIN({ navController -> navController.navigate("loadinScreen") }),


}