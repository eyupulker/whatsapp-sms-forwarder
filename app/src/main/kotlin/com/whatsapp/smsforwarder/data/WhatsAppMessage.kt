package com.whatsapp.smsforwarder.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Data class representing a WhatsApp message extracted from notification
 */
@Parcelize
data class WhatsAppMessage(
    val sender: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis(),
    val chatTitle: String? = null
) : Parcelable

/**
 * Data class for SMS forwarding configuration
 */
@Parcelize
data class ForwardingConfig(
    val targetPhoneNumber: String,
    val isEnabled: Boolean = false,
    val includeSender: Boolean = true,
    val includeTimestamp: Boolean = true,
    val customPrefix: String = "WhatsApp: "
) : Parcelable

/**
 * Result wrapper for operations
 */
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable) : Result<Nothing>()
    object Loading : Result<Nothing>()
}
