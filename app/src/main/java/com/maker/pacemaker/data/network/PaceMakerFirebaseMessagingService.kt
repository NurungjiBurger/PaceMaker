package com.maker.pacemaker.data.network

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.provider.ContactsContract
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.viewModelScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.maker.pacemaker.R
import com.maker.pacemaker.data.model.db.AlarmDao
import com.maker.pacemaker.data.model.db.AlarmEntity
import com.maker.pacemaker.data.model.worker.SaveAlarmWorker
import com.maker.pacemaker.ui.activity.main.MainActivity
import com.maker.pacemaker.ui.viewmodel.main.MainBaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PaceMakerFirebaseMessagingService : FirebaseMessagingService {

    @Inject
    lateinit var alarmDao: AlarmDao // AlarmDao 주입

    // 기본 생성자
    constructor() : super()

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("FCM", "메시지 수신: ${remoteMessage.notification}")
        Log.d("FCM", "메시지 수신: ${remoteMessage.data}")

        // 알림 메시지 처리
        remoteMessage.notification?.let {
            Log.d("FCM", "알림 메시지: ${it.title}, ${it.body}")
            sendNotification(it.title, it.body)
        }
    }

    private fun sendNotification(title: String?, messageBody: String?) {
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)

        val channelId = "default_channel_id"
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.logo) // 알림 아이콘
            .setContentTitle(title) // 알림 제목
            .setContentText(messageBody) // 알림 내용
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH) // 중요도를 높게 설정

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Android 8.0 (API 26) 이상에서는 NotificationChannel이 필요합니다.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "Default Channel"
            val channelDescription = "Channel for default notifications"
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH).apply {
                description = channelDescription
            }
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notificationBuilder.build())

        // 알람 저장 (예: 타이틀과 내용 저장)
        saveAlarm(title!!, messageBody!!)
    }

//    private fun saveAlarm(title: String, message: String) {
//        val data = ContactsContract.Contacts.Data.Builder()
//            .putString("title", title)
//            .putString("message", message)
//            .build()
//
//        val workRequest = OneTimeWorkRequestBuilder<SaveAlarmWorker>()
//            .setInputData(data)
//            .build()
//
//        WorkManager.getInstance(this).enqueue(workRequest)
//    }

    private fun saveAlarm(title: String, message: String) {
        CoroutineScope(Dispatchers.IO).launch {
            // 데이터베이스에 알람 저장
            val newAlarm = AlarmEntity(alarmType = title, content = message, dateTime = System.currentTimeMillis())
            alarmDao.insertAlarm(newAlarm)

            Log.d("FCM", "알람 저장 완료: $newAlarm")
            val intent = Intent("com.maker.pacemaker.ALARM_UPDATED")
            LocalBroadcastManager.getInstance(this@PaceMakerFirebaseMessagingService).sendBroadcast(intent)
        }
    }

    override fun onNewToken(token: String) {
        Log.d("FCM", "새 FCM 토큰: $token")
        saveTokenToServer(token)
    }

    private fun saveTokenToServer(token: String) {
        val sharedPreferences: SharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("fcmToken", token)
        editor.apply()

        Log.d("FCM", "새 FCM 토큰 저장 완료: $token")
    }
}
