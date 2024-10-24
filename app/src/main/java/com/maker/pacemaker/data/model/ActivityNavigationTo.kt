package com.maker.pacemaker.data.model

import android.content.Context
import android.content.Intent
import com.maker.pacemaker.ui.activity.main.MainActivity
import com.maker.pacemaker.ui.activity.sign.SignUpActivity

data class ActivityNavigationTo(val activityType: ActivityType)

// Enum 클래스에서 Intent 정보를 관리하도록 수정
enum class ActivityType(val intentCreator: (Context) -> Intent?) {
    FINISH({ null }),

    SIGNUP ({ context -> Intent(context, SignUpActivity::class.java) }),
    MAIN({ context -> Intent(context, MainActivity::class.java) }),
}