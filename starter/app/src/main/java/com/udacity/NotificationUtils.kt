package com.udacity

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

fun NotificationManager.sendNotification(fileName:String,status:String, applicationContext: Context){

    val intent = Intent(applicationContext,DetailActivity::class.java)
    intent.putExtra(FILE_NAME_KEY,fileName)
    intent.putExtra(STATUS_KEY,status)
    val pendingIntent = PendingIntent.getActivity(
        applicationContext,NOTIFICATION_ID,intent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val builder = NotificationCompat.Builder(applicationContext,CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setContentTitle(applicationContext.getString(R.string.notification_title))
        .setContentText(applicationContext.getString(R.string.notification_description))
        .addAction(R.drawable.ic_assistant_black_24dp,applicationContext.getString(R.string.notification_button),pendingIntent)

    notify(NOTIFICATION_ID,builder.build())

}