package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.widget.RadioGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.udacity.utils.Constants.Companion.CHANNEL_ID
import com.udacity.utils.sendNotification
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.io.File


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val radioGroup = findViewById<RadioGroup>(R.id.rgLinks)
        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        notificationManager = ContextCompat.getSystemService(
            applicationContext,
            NotificationManager::class.java
        ) as NotificationManager

        createChannel(CHANNEL_ID, "Meow")

        custom_button.setOnClickListener {
            val checkedRadioButton = findViewById<MyRadioButton>(radioGroup.checkedRadioButtonId)
            if(checkedRadioButton == null)
              Toast.makeText(this, "Please select one of the options above", Toast.LENGTH_SHORT).show()

           download(checkedRadioButton.linkText)

        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun download(URL: String) {
        val subPath = File(URL).name


        val request =
            DownloadManager.Request(Uri.parse(URL))
                .setTitle(subPath)
                .setDescription(getString(R.string.app_description))
                .setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS, subPath
                )
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
        val query = DownloadManager.Query()

        query.setFilterById(downloadID)


        val onComplete: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(ctxt: Context, intent: Intent) {

                val cursor = downloadManager.query(query)

                var status = "FAIL"
                var title = ""
                if (cursor.moveToFirst()){
                    when (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
                        DownloadManager.STATUS_SUCCESSFUL ->{
                            status = "SUCCESS"
                            title = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TITLE));
                        }
                        DownloadManager.STATUS_FAILED -> {
                            status = "FAIL"
                        }
                    }
                }
                notificationManager.sendNotification(
                    ctxt.getString(R.string.notification_description),
                    ctxt,
                    title,
                    status
                )

            }
        }
        registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

    }

    private fun createChannel(channelId: String, channelName: String) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                // TODO: Step 2.4 change importance
                NotificationManager.IMPORTANCE_HIGH
            )
            // TODO: Step 2.6 disable badges for this channel

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "File download complete"

            notificationManager.createNotificationChannel(notificationChannel)
        }
    }



}
