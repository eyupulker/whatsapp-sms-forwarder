package com.whatsapp.smsforwarder.util

import android.app.Notification
import android.os.Bundle
import android.service.notification.StatusBarNotification
import com.whatsapp.smsforwarder.data.WhatsAppMessage
import timber.log.Timber

/**
 * Utility class to parse WhatsApp notifications and extract message information
 */
object NotificationParser {
    
    private const val WHATSAPP_PACKAGE = "com.whatsapp"
    private const val WHATSAPP_BUSINESS_PACKAGE = "com.whatsapp.w4b"
    
    /**
     * Check if the notification is from WhatsApp
     */
    fun isWhatsAppNotification(sbn: StatusBarNotification): Boolean {
        val packageName = sbn.packageName
        return packageName == WHATSAPP_PACKAGE || packageName == WHATSAPP_BUSINESS_PACKAGE
    }
    
    /**
     * Extract WhatsApp message from notification
     */
    fun extractWhatsAppMessage(sbn: StatusBarNotification): WhatsAppMessage? {
        return try {
            val notification = sbn.notification
            val extras = notification.extras
            
            // Extract sender name
            val sender = extractSender(extras, notification)
            
            // Extract message content
            val message = extractMessage(extras, notification)
            
            // Extract chat title if available
            val chatTitle = extractChatTitle(extras)
            
            if (sender.isNotEmpty() && message.isNotEmpty()) {
                WhatsAppMessage(
                    sender = sender,
                    message = message,
                    timestamp = sbn.postTime,
                    chatTitle = chatTitle
                )
            } else {
                Timber.w("Failed to extract complete message data from notification")
                null
            }
        } catch (e: Exception) {
            Timber.e(e, "Error extracting WhatsApp message from notification")
            null
        }
    }
    
    private fun extractSender(extras: Bundle, notification: Notification): String {
        // Try different methods to extract sender name
        return extras.getString(Notification.EXTRA_TITLE) 
            ?: extras.getString("android.title")
            ?: notification.extras.getString(Notification.EXTRA_TITLE)
            ?: ""
    }
    
    private fun extractMessage(extras: Bundle, notification: Notification): String {
        // Try different methods to extract message content
        return extras.getString(Notification.EXTRA_TEXT)
            ?: extras.getString("android.text")
            ?: notification.extras.getString(Notification.EXTRA_TEXT)
            ?: extras.getString(Notification.EXTRA_BIG_TEXT)
            ?: ""
    }
    
    private fun extractChatTitle(extras: Bundle): String? {
        return extras.getString(Notification.EXTRA_SUB_TEXT)
            ?: extras.getString("android.subText")
    }
    
    /**
     * Format message for SMS forwarding
     */
    fun formatMessageForSMS(
        whatsAppMessage: WhatsAppMessage,
        config: com.whatsapp.smsforwarder.data.ForwardingConfig
    ): String {
        val prefix = config.customPrefix
        val sender = if (config.includeSender) "${whatsAppMessage.sender}: " else ""
        val timestamp = if (config.includeTimestamp) {
            val time = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())
                .format(java.util.Date(whatsAppMessage.timestamp))
            " [$time]"
        } else ""
        
        return "$prefix$sender${whatsAppMessage.message}$timestamp"
    }
}
