package com.maker.pacemaker.utils

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat

object PermissionsHelper {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val requiredPermissions =
        arrayOf(
            Manifest.permission.POST_NOTIFICATIONS
        )

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun hasAllPermissions(activity: ComponentActivity): Boolean {
        return requiredPermissions.all {
            ActivityCompat.checkSelfPermission(activity, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun requestPermissions(activity: ComponentActivity) {
        ActivityCompat.requestPermissions(activity, requiredPermissions, 0)
    }

    fun allPermissionsGranted(grantResults: IntArray): Boolean {
        return grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }
    }
}