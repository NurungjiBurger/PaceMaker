package com.maker.pacemaker.ui.viewmodel.boot.details

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.lifecycle.ViewModel
import com.maker.pacemaker.ui.viewmodel.boot.BootBaseViewModel
import com.maker.pacemaker.ui.viewmodel.main.MainBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
open class BootScreenViewModel @Inject constructor(
    private val base: MainBaseViewModel
): ViewModel() {

    val baseViewModel = base

    val fireBaseUID = baseViewModel.baseViewModel.sharedPreferences.getString("fireBaseUID", "")

    private val _isPermissionGranted = MutableStateFlow(false)
    val isPermissionGranted = _isPermissionGranted

    init {
        Log.d("FCM", "FireBase UID: $fireBaseUID")


    }


    fun updatePermissionGranted(granted: Boolean) {
        baseViewModel.baseViewModel.editor.putBoolean("permissionGranted", granted)
        baseViewModel.baseViewModel.editor.apply()

        _isPermissionGranted.value = granted
    }

}