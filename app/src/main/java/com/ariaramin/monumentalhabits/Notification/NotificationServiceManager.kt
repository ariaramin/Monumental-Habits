package com.ariaramin.monumentalhabits.Notification

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.ariaramin.monumentalhabits.MainActivity
import com.ariaramin.monumentalhabits.Models.Habit
import com.ariaramin.monumentalhabits.R
import com.ariaramin.monumentalhabits.Receiver.NotificationReceiver
import com.ariaramin.monumentalhabits.Utils.Constants
import java.util.*


class NotificationServiceManager {

    fun scheduleNotification(context: Context, habit: Habit) {
        val intent = Intent(context, NotificationReceiver::class.java)
        intent.putExtra(Constants.HABIT, habit)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            (Constants.NOTIFICATION_ID + habit.id).toInt(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val currentApiVersion = Build.VERSION.SDK_INT
        if (currentApiVersion < Build.VERSION_CODES.KITKAT) {
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                getTime(habit),
                pendingIntent
            )
        } else {
            if (currentApiVersion < Build.VERSION_CODES.M) {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    getTime(habit),
                    pendingIntent
                )
            } else {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    getTime(habit),
                    pendingIntent
                )
            }
        }

    }

    private fun getTime(habit: Habit): Long {
        val reminder = habit.reminderTime
        val hour = reminder.subSequence(0, reminder.indexOf(":")).toString()
        val minute = reminder.subSequence(reminder.indexOf(":") + 1, reminder.length - 3).toString()
        val amOrPm = reminder.subSequence(reminder.length - 2, reminder.length).toString()
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour.toInt())
        calendar.set(Calendar.MINUTE, minute.toInt())
        calendar.set(Calendar.SECOND, 0)
        if (amOrPm == Constants.AM) {
            calendar.set(Calendar.AM_PM, Calendar.AM)
        } else {
            calendar.set(Calendar.AM_PM, Calendar.PM)
        }
        return calendar.timeInMillis
    }

    fun showNotification(context: Context, habit: Habit) {
        val notification = NotificationCompat.Builder(context, Constants.CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText(context.getString(R.string.notification_message) + " " + habit.title)
            .setContentIntent(getPendingIntent(context, habit))
            .build()
        val manager = getNotificationManager(context)
        manager.notify((Constants.NOTIFICATION_ID + habit.id).toInt(), notification)
    }

    private fun getPendingIntent(context: Context, habit: Habit): PendingIntent {
        val bundle = Bundle()
        bundle.putParcelable(Constants.HABIT, habit)
        return NavDeepLinkBuilder(context)
            .setComponentName(MainActivity::class.java)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.habitFragment)
            .setArguments(bundle)
            .createPendingIntent()
    }

    fun createNotificationChannel(context: Context) {
        val channelName = context.getString(R.string.channel_name)
        val channelDescription = context.getString(R.string.channel_description)
        val channel = NotificationChannel(
            Constants.CHANNEL_ID,
            channelName,
            NotificationManager.IMPORTANCE_HIGH
        )
        channel.description = channelDescription
//        channel.setSound()
        val manager = getNotificationManager(context)
        manager.createNotificationChannel(channel)
    }

    private fun getNotificationManager(context: Context): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }
}