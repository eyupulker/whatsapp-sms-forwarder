package com.whatsapp.smsforwarder.util

import android.content.Context
import android.content.SharedPreferences
import com.whatsapp.smsforwarder.data.ForwardingConfig
import timber.log.Timber

/**
 * Manages app preferences and configuration
 */
class PreferenceManager(context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME, Context.MODE_PRIVATE
    )
    
    companion object {
        private const val PREFS_NAME = "whatsapp_sms_forwarder_prefs"
        private const val KEY_TARGET_PHONE = "target_phone_number"
        private const val KEY_IS_ENABLED = "is_enabled"
        private const val KEY_INCLUDE_SENDER = "include_sender"
        private const val KEY_INCLUDE_TIMESTAMP = "include_timestamp"
        private const val KEY_CUSTOM_PREFIX = "custom_prefix"
        private const val KEY_NOTIFICATION_ACCESS_GRANTED = "notification_access_granted"
        private const val KEY_SMS_PERMISSION_GRANTED = "sms_permission_granted"
    }
    
    /**
     * Save forwarding configuration
     */
    fun saveForwardingConfig(config: ForwardingConfig) {
        prefs.edit().apply {
            putString(KEY_TARGET_PHONE, config.targetPhoneNumber)
            putBoolean(KEY_IS_ENABLED, config.isEnabled)
            putBoolean(KEY_INCLUDE_SENDER, config.includeSender)
            putBoolean(KEY_INCLUDE_TIMESTAMP, config.includeTimestamp)
            putString(KEY_CUSTOM_PREFIX, config.customPrefix)
            apply()
        }
        Timber.d("Forwarding configuration saved")
    }
    
    /**
     * Get forwarding configuration
     */
    fun getForwardingConfig(): ForwardingConfig {
        return ForwardingConfig(
            targetPhoneNumber = prefs.getString(KEY_TARGET_PHONE, "") ?: "",
            isEnabled = prefs.getBoolean(KEY_IS_ENABLED, false),
            includeSender = prefs.getBoolean(KEY_INCLUDE_SENDER, true),
            includeTimestamp = prefs.getBoolean(KEY_INCLUDE_TIMESTAMP, true),
            customPrefix = prefs.getString(KEY_CUSTOM_PREFIX, "WhatsApp: ") ?: "WhatsApp: "
        )
    }
    
    /**
     * Set notification access granted status
     */
    fun setNotificationAccessGranted(granted: Boolean) {
        prefs.edit().putBoolean(KEY_NOTIFICATION_ACCESS_GRANTED, granted).apply()
    }
    
    /**
     * Check if notification access is granted
     */
    fun isNotificationAccessGranted(): Boolean {
        return prefs.getBoolean(KEY_NOTIFICATION_ACCESS_GRANTED, false)
    }
    
    /**
     * Set SMS permission granted status
     */
    fun setSMSPermissionGranted(granted: Boolean) {
        prefs.edit().putBoolean(KEY_SMS_PERMISSION_GRANTED, granted).apply()
    }
    
    /**
     * Check if SMS permission is granted
     */
    fun isSMSPermissionGranted(): Boolean {
        return prefs.getBoolean(KEY_SMS_PERMISSION_GRANTED, false)
    }
    
    /**
     * Clear all preferences
     */
    fun clearAll() {
        prefs.edit().clear().apply()
        Timber.d("All preferences cleared")
    }
}
