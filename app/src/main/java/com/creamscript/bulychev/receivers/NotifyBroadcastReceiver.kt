package com.creamscript.bulychev.receivers

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.creamscript.bulychev.R
import com.creamscript.bulychev.activities.MainActivity
import com.creamscript.bulychev.utils.nextBirthday
import java.text.SimpleDateFormat
import java.util.*
import com.creamscript.bulychev.data.CONTACT_ID
import com.creamscript.bulychev.data.CONTACT_NAME
import com.creamscript.bulychev.data.DATE_FORMAT_BIRTHDAY
import com.creamscript.bulychev.data.FRAGMENT_ID

private const val CHANNEL_ID = "NOTIFY_BIRTHDAY_CHANNEL"

class NotifyBroadcastReceiver : BroadcastReceiver() {

    @SuppressLint("SimpleDateFormat")
    override fun onReceive(context: Context, intent: Intent) {

        Log.d("RECEIVER", "onReceive")

        val notificationManager: NotificationManager
                = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                    CHANNEL_ID,
                    "Notify Birthday Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val notificationIntent = Intent(context, MainActivity::class.java)
        notificationIntent.putExtra(FRAGMENT_ID, "contactDetailsLayout");

        val pendingIntent = PendingIntent.getActivity(
                                context,
                                0,
                                notificationIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(context.getString(R.string.app_name))
                .setChannelId(CHANNEL_ID)
                .setContentText(context.getString(R.string.text_notify_birthday)
                        + " "
                        + intent.getStringExtra(CONTACT_NAME))
                .setSmallIcon(android.R.drawable.ic_menu_my_calendar)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .build()

        val contactId = intent.getIntExtra(CONTACT_ID, 0)

        notificationManager.notify(contactId, notification)

        val currentTime = Date()
        val dateFormatBirthday= SimpleDateFormat(DATE_FORMAT_BIRTHDAY)
        val calendar = nextBirthday(dateFormatBirthday.format(currentTime))

        val reschedulePendingIntent = PendingIntent.getBroadcast(context, contactId, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        alarmManager?.set(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                reschedulePendingIntent
        )
    }

}