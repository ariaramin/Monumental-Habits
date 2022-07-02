package com.ariaramin.monumentalhabits.Receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.ariaramin.monumentalhabits.Models.Habit
import com.ariaramin.monumentalhabits.Notification.NotificationServiceManager
import com.ariaramin.monumentalhabits.Utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NotificationReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationServiceManager: NotificationServiceManager

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let { appContext ->
            val extras = intent?.extras
            val habit = extras?.getParcelable<Habit>(Constants.HABIT)
            habit?.let {
                notificationServiceManager.showNotification(appContext, it)
            }
        }
    }
}