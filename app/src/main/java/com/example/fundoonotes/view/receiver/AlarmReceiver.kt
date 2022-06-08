package com.example.fundoonotes.view.receiver

import android.app.Notification
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.fundoonotes.R
import com.example.fundoonotes.model.Note

private const val TAG = "AlarmReceiver"
class AlarmReceiver(): BroadcastReceiver() {
//private val note: Note
    override fun onReceive(context: Context, intent: Intent?) {
        Log.d(TAG, "Alarm Received!")
        val reminderNotification = NotificationCompat.Builder(context, "fundooReminder")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Fundoo Notes")
            .setContentText("Reminder")
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notificationManager = NotificationManagerCompat.from(context)

        notificationManager.notify(18225, reminderNotification.build())
    }
}