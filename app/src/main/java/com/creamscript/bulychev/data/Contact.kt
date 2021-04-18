package com.creamscript.bulychev.data

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.annotation.DrawableRes
import com.creamscript.bulychev.receivers.NotifyBroadcastReceiver
import com.creamscript.bulychev.receivers.NotifyBroadcastReceiver.Companion.CONTACT_ID
import com.creamscript.bulychev.receivers.NotifyBroadcastReceiver.Companion.CONTACT_NAME
import java.util.*

data class Contact(
        val contactName: String,
        val firstPhone: String,
        val secondPhone: String,
        val firstEmail: String,
        val secondEmail: String,
        val contactDescription: String,
        @DrawableRes val photoResId: Int,
        val dateBirthday: Calendar
) {
    fun scheduleNotify(context: Context, calendar: Calendar) {

        val intent = Intent(context, NotifyBroadcastReceiver::class.java)

        intent.putExtra(CONTACT_ID, 0)
        intent.putExtra(CONTACT_NAME, contactName)

        val alarmIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        alarmManager?.set(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                alarmIntent
        )

        Log.d("CONTACT", "scheduleNotify")
    }

    fun stopNotify(context: Context) {

        val intent = Intent(context, NotifyBroadcastReceiver::class.java)
        val alarmIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        alarmManager?.cancel(alarmIntent)
    }

    fun isAlarmSet(context: Context): Boolean {

        val intent = Intent(context, NotifyBroadcastReceiver::class.java)
        val alarmIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        return alarmIntent != null
    }
}