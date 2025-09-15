package com.whatsapp.smsforwarder.service

import android.content.Intent
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.whatsapp.smsforwarder.data.ForwardingConfig
import com.whatsapp.smsforwarder.util.NotificationParser
import com.whatsapp.smsforwarder.util.SMSManager
import com.whatsapp.smsforwarder.util.PreferenceManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Service that listens to notifications and forwards WhatsApp messages as SMS
 */
class WhatsAppNotificationListenerService : NotificationListenerService() {
    
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private lateinit var smsManager: SMSManager
    private lateinit var preferenceManager: PreferenceManager
    
    override fun onCreate() {
        super.onCreate()
        smsManager = SMSManager(this)
        preferenceManager = PreferenceManager(this)
        Timber.d("WhatsAppNotificationListenerService created")
    }
    
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        super.onNotificationPosted(sbn)
        
        // Check if it's a WhatsApp notification
        if (!NotificationParser.isWhatsAppNotification(sbn)) {
            return
        }
        
        // Check if forwarding is enabled
        val config = preferenceManager.getForwardingConfig()
        if (!config.isEnabled) {
            Timber.d("Forwarding is disabled, ignoring notification")
            return
        }
        
        // Extract WhatsApp message
        val whatsAppMessage = NotificationParser.extractWhatsAppMessage(sbn)
        if (whatsAppMessage == null) {
            Timber.w("Failed to extract WhatsApp message from notification")
            return
        }
        
        Timber.d("Extracted WhatsApp message: ${whatsAppMessage.sender} - ${whatsAppMessage.message}")
        
        // Forward as SMS
        serviceScope.launch {
            val result = smsManager.forwardWhatsAppMessage(whatsAppMessage, config)
            when (result) {
                is com.whatsapp.smsforwarder.data.Result.Success -> {
                    Timber.d("Successfully forwarded WhatsApp message as SMS")
                }
                is com.whatsapp.smsforwarder.data.Result.Error -> {
                    Timber.e(result.exception, "Failed to forward WhatsApp message as SMS")
                }
                else -> {
                    Timber.w("Unexpected result when forwarding message")
                }
            }
        }
    }
    
    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        super.onNotificationRemoved(sbn)
        // We don't need to handle notification removal for this use case
    }
    
    override fun onDestroy() {
        super.onDestroy()
        serviceScope.coroutineContext.cancel()
        Timber.d("WhatsAppNotificationListenerService destroyed")
    }
    
    companion object {
        const val ACTION_START_FORWARDING = "com.whatsapp.smsforwarder.START_FORWARDING"
        const val ACTION_STOP_FORWARDING = "com.whatsapp.smsforwarder.STOP_FORWARDING"
    }
}
