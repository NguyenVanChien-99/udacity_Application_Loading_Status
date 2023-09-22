package com.udacity

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.udacity.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    private var detailTitle = ""

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))



        notificationManager = ContextCompat.getSystemService(
            this,
            NotificationManager::class.java
        ) as NotificationManager

        createChannel(CHANNEL_ID, CHANNEL_NAME)


        binding.contentMain.customButton.setOnClickListener {

            if (binding.contentMain.radio.checkedRadioButtonId != -1) {
                binding.contentMain.customButton.setNewState(ButtonState.Clicked)
            }

            when (binding.contentMain.radio.checkedRadioButtonId) {
                R.id.btnGlide -> {
                    download(GLIDE)
                    detailTitle = resources.getString(R.string.glide_text)
                }

                R.id.btnLoadApp -> {
                    download(LOADAPP)
                    detailTitle = resources.getString(R.string.load_app_text)
                }

                R.id.btnRetrofit -> {
                    download(RETROFIT)
                    detailTitle = resources.getString(R.string.retrofit_text)
                }

                else -> {
                    Toast.makeText(this, "Please select the file to download", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (id == downloadID) {
                val status = getDownloadStatus()
//                notificationManager.cancelAll()
                notificationManager.sendNotification(detailTitle, status, applicationContext)
            }
        }
    }

    private fun getDownloadStatus(): String {
        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        val query = DownloadManager.Query()
        query.setFilterById(downloadID)
        val cursor: Cursor = downloadManager.query(query)

        var status = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
        if (cursor.moveToNext()) {
            status = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
        }
        return when (cursor.getInt(status)) {
            DownloadManager.STATUS_SUCCESSFUL -> "Success"
            else -> "Fail"
        }
    }

    private fun download(url: String) {
        val request =
            DownloadManager.Request(Uri.parse(url))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }


    private fun createChannel(channelID: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelID,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.enableVibration(true)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED

            val notificationManger = this.getSystemService(NotificationManager::class.java)
            notificationManger.createNotificationChannel(notificationChannel)
        }
    }
}