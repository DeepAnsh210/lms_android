package com.example.focus


import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.*

class FocusTimerService : Service() {

    private val channelId = "FocusTimerServiceChannel"
    private var beepJob: Job? = null
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate() {
        super.onCreate()
        val notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        mediaPlayer = MediaPlayer.create(this, notificationSoundUri)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val intervalInMinutes = intent?.getLongExtra("interval", 1) ?: 1
        val soundResId = intent?.getIntExtra("soundResId", R.raw.alertsound) ?: R.raw.alertsound

        startForegroundService()
        startBeeping(intervalInMinutes, soundResId)
        return START_NOT_STICKY
    }

    private fun startForegroundService() {
        createNotificationChannel()

        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val notification: Notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Focus Timer")
            .setContentText("The focus timer is running")
//            .setSmallIcon(R.drawable.ic_timer)  // Replace with your app icon
            .setContentIntent(pendingIntent)
            .build()

        startForeground(1, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                channelId,
                "Focus Timer Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    private fun startBeeping(intervalInMinutes: Long, soundResId: Int) {
        stopBeeping()
        mediaPlayer = MediaPlayer.create(this, soundResId)
        beepJob = CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
                mediaPlayer.start()
                delay(intervalInMinutes * 60 * 1000)
            }
        }
    }

    private fun stopBeeping() {
        beepJob?.cancel()
    }

    private fun beepNow() {
        mediaPlayer.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopBeeping()
        mediaPlayer.release()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
