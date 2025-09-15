package com.whatsapp.smsforwarder.util

import android.content.Context
import android.telephony.SmsManager
import com.whatsapp.smsforwarder.data.ForwardingConfig
import com.whatsapp.smsforwarder.data.WhatsAppMessage
import com.whatsapp.smsforwarder.data.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * Utility class for sending SMS messages
 */
class SMSManager(private val context: Context) {
    
    private val smsManager = SmsManager.getDefault()
    
    /**
     * Send WhatsApp message as SMS
     */
    suspend fun forwardWhatsAppMessage(
        whatsAppMessage: WhatsAppMessage,
        config: ForwardingConfig
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val message = NotificationParser.formatMessageForSMS(whatsAppMessage, config)
            
            // Split message if it's too long for SMS
            val messages = smsManager.divideMessage(message)
            
            if (messages.size == 1) {
                smsManager.sendTextMessage(
                    config.targetPhoneNumber,
                    null,
                    message,
                    null,
                    null
                )
            } else {
                // Send multipart SMS
                smsManager.sendMultipartTextMessage(
                    config.targetPhoneNumber,
                    null,
                    messages,
                    null,
                    null
                )
            }
            
            Timber.d("Successfully forwarded WhatsApp message to ${config.targetPhoneNumber}")
            Result.Success(Unit)
            
        } catch (e: Exception) {
            Timber.e(e, "Failed to send SMS")
            Result.Error(e)
        }
    }
    
    /**
     * Check if SMS sending is available
     */
    fun isSMSAvailable(): Boolean {
        return try {
            smsManager != null
        } catch (e: Exception) {
            Timber.e(e, "SMS not available")
            false
        }
    }
    
    /**
     * Validate phone number format
     */
    fun isValidPhoneNumber(phoneNumber: String): Boolean {
        return phoneNumber.matches(Regex("^\\+?[1-9]\\d{1,14}$"))
    }
}
