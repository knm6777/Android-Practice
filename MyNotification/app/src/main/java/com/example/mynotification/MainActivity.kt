package com.example.mynotification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createNotificationChannel1()
        createNotificationChannel1()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.noti1 -> showNotification1()
            R.id.noti2 -> showNotification2()
        }
        return super.onOptionsItemSelected(item)
    }
//
    private val channelID1 = "default"
    private val channelID2 = "notice"


    private fun createNotificationChannel1() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // Android 8.0
            val channel = NotificationChannel(
                channelID1, "default channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = "description text of this channel."
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotificationChannel2() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // Android 8.0
            val channel = NotificationChannel(
                channelID2, "notice channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = "description text of this channel."
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private var myNotificationID = 1
        get() = field++


    private fun showNotification1() {
        val intent = Intent(this, Noti1Activity::class.java)
        val pendingIntent = with (TaskStackBuilder.create(this)) {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }
        val builder = NotificationCompat.Builder(this, channelID1)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Notification1")
            .setContentText("Notification1 body")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true) // auto remove this notification when user touches it
        NotificationManagerCompat.from(this)
            .notify(myNotificationID, builder.build())
    }


    private fun showNotification2() {
        val intent = Intent(this, Noti2Activity::class.java)
        val pendingIntent = with (TaskStackBuilder.create(this)) {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }
        val builder = NotificationCompat.Builder(this, channelID2)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Notification2")
            .setContentText("Notification2 body")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true) // auto remove this notification when user touches it
        NotificationManagerCompat.from(this)
            .notify(myNotificationID, builder.build())
    }

}