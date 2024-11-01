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
    SIGNUP({ navController -> navController.navigate("signUpScreen") }),
    SIGNUPLOAD({ navController -> navController.navigate("signUpLoadScreen") }),

    // 메인
    MAIN({ navController -> navController.navigate("mainScreen") }),
    MENU({ navController -> navController.navigate("menuScreen") }),
    ALARM({ navController -> navController.navigate("alarmScreen") }),
    PROBLEMADD({ navController -> navController.navigate("problemAddScreen") }),
    PROBLEMSEARCH({ navController -> navController.navigate("problemSearchScreen") }),
    RANKING({ navController -> navController.navigate("rankingScreen") }),
    CSMANTLE({ navController -> navController.navigate("csMantleScreen") }),
    LAB({ navController -> navController.navigate("labScreen") }),

    // 로그인
    SIGNIN({ navController -> navController.navigate("signInScreen") }),
    SIGNINLOAD({ navController -> navController.navigate("signInLoadScreen") }),

    // 세팅
    MYPAGE({ navController -> navController.navigate("mypageScreen") }),
    DAILY({ navController -> navController.navigate("dailyScreen") }),
    RATIO({ navController -> navController.navigate("ratioScreen") }),
    CATEGORY({ navController -> navController.navigate("categoryScreen") }),
    LEVELTEST({ navController -> navController.navigate("levelTestScreen") }),
    PROBLEMSOLVE({ navController -> navController.navigate("problemSolveScreen") }),

}