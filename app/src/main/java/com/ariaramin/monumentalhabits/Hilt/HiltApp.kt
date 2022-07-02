package com.ariaramin.monumentalhabits.Hilt

import android.app.Application
import com.ariaramin.monumentalhabits.Notification.NotificationServiceManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class HiltApp : Application() {

    @Inject
    lateinit var notificationServiceManager: NotificationServiceManager

    override fun onCreate() {
        super.onCreate()
        notificationServiceManager.createNotificationChannel(applicationContext)
    }
}