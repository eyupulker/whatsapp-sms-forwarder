package com.whatsapp.smsforwarder.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.whatsapp.smsforwarder.util.PreferenceManager
import timber.log.Timber

/**
 * Background service for SMS forwarding operations
 */
class SMSForwardingService : Service() {
    
    private lateinit var preferenceManager: PreferenceManager
    
    override fun onCreate() {
        super.onCreate()
        preferenceManager = PreferenceManager(this)
        Timber.d("SMSForwardingService created")
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START_FORWARDING -> {
                startForegroundService()
                Timber.d("SMS forwarding service started")
            }
            ACTION_STOP_FORWARDING -> {
                stopForegroundService()
                Timber.d("SMS forwarding service stopped")
            }
        }
        return START_STICKY
    }
    
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
    
    private fun startForegroundService() {
        // Create notification for foreground service
        val notification = createForegroundNotification()
        startForeground(NOTIFICATION_ID, notification)
    }
    
    private fun stopForegroundService() {
        stopForeground(true)
        stopSelf()
    }
    
    private fun createForegroundNotification(): android.app.Notification {
        return android.app.Notification.Builder(this, CHANNEL_ID)
            .setContentTitle("WhatsApp SMS Forwarder")
            .setContentText("Forwarding WhatsApp messages as SMS")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setOngoing(true)
            .build()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        Timber.d("SMSForwardingService destroyed")
    }
    
    companion object {
        const val ACTION_START_FORWARDING = "com.whatsapp.smsforwarder.START_FORWARDING"
        const val ACTION_STOP_FORWARDING = "com.whatsapp.smsforwarder.STOP_FORWARDING"
        const val CHANNEL_ID = "sms_forwarding_channel"
        const val NOTIFICATION_ID = 1001
    }
}
