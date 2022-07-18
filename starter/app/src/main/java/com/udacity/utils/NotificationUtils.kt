 package com.udacity.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.udacity.DetailActivity
import com.udacity.R
import com.udacity.utils.Constants.Companion.CHANNEL_ID

private val NOTIFICATION_ID = 0
fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context, fileName:String, downloadStatus:String) {

    val detailIntent = Intent(applicationContext, DetailActivity::class.java)
    detailIntent.putExtra("fileName", fileName)
    detailIntent.putExtra("downloadStatus", downloadStatus)
    val detailPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        detailIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val builder = NotificationCompat.Builder(
        applicationContext,
        CHANNEL_ID
    ).setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle(
            applicationContext
                .getString(R.string.notification_title)
        ).addAction(R.drawable.ic_launcher_foreground, applicationContext.getString(R.string.notification_button) ,detailPendingIntent )
        .setContentText(messageBody).setAutoCancel(true)


    notify(NOTIFICATION_ID, builder.build())
}

