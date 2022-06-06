package com.example.fundoonotes.view.ui

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.fundoonotes.R
import com.example.fundoonotes.view.receiver.AlarmReceiver
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*

class SetReminder: DialogFragment(R.layout.fragment_dialog_reminder) {

    private lateinit var btnBack : ImageView
    private lateinit var btnSave: TextView
    private lateinit var btnSetTime: TextView
    private lateinit var btnSetDate: TextView
    private lateinit var timePicker: MaterialTimePicker
    private lateinit var tvTime : TextView
    private lateinit var tvDate : TextView
    private var reminderTime = Calendar.getInstance()
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createNotificationChannel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnBack = requireView().findViewById(R.id.btnBackReminder)
        btnSave = requireView().findViewById(R.id.btnSaveAlarm)
        btnSetTime = requireView().findViewById(R.id.btnSetTime)
        btnSetDate = requireView().findViewById(R.id.btnSetDate)
        tvTime = requireView().findViewById(R.id.tvTime)
        tvDate = requireView().findViewById(R.id.tvDate)
    }

    override fun onStart() {
        super.onStart()

        btnBack.setOnClickListener{
            dismiss()
        }

        btnSave.setOnClickListener{
            setAlarm()
            dismiss()
            Toast.makeText(this.context, "Reminder Set!", Toast.LENGTH_SHORT).show()
        }

        btnSetTime.setOnClickListener{
            showTimePicker()
        }

        btnSetDate.setOnClickListener{
            showDatePicker()
        }
    }

    private fun setAlarm() {
        alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intentToAlarmReceiver = Intent(this.context, AlarmReceiver()::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            pendingIntent = PendingIntent.getBroadcast(this.context, 0, intentToAlarmReceiver, PendingIntent.FLAG_IMMUTABLE)
        }
        alarmManager.set(AlarmManager.RTC_WAKEUP, reminderTime.timeInMillis, pendingIntent)
//        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, reminderTime.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
    }

    private fun showTimePicker() {
        timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(12)
            .setMinute(0)
            .setTitleText("Pick Time")
            .build()

        timePicker.show(requireActivity().supportFragmentManager, "Set Reminder")

        timePicker.addOnPositiveButtonClickListener{
            if(timePicker.hour > 12){
                tvTime.text = String.format("${(timePicker.hour - 12)}:${timePicker.minute} pm")
            }else{
                tvTime.text = String.format("${timePicker.hour}:${timePicker.minute} am")
            }

            reminderTime.set(Calendar.HOUR_OF_DAY,timePicker.hour)
            reminderTime.set(Calendar.MINUTE, timePicker.minute)
            reminderTime.set(Calendar.SECOND, 0)
            reminderTime.set(Calendar.MILLISECOND, 0)
        }

        timePicker.addOnCancelListener {
            dismiss()
        }
    }

    private fun showDatePicker() {
        val datePicker = MaterialDatePicker.Builder
            .datePicker()
            .setTitleText("Pick Date")
            .build()

        datePicker.show(requireActivity().supportFragmentManager, "Set Time")

        datePicker.addOnPositiveButtonClickListener {
            tvDate.text = String.format(datePicker.headerText)
        }
        datePicker.addOnCancelListener{
            dismiss()
        }
    }

    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "Fundoo Note Reminder"
            val desc = "Channel for alarm Manager"
            val importance = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(getString(R.string.reminder_channel_id), name, importance)
            channel.description = desc

            val notificationManager = requireActivity().getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}