package com.whatsapp.smsforwarder.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.whatsapp.smsforwarder.service.SMSForwardingService
import com.whatsapp.smsforwarder.util.PreferenceManager
import timber.log.Timber

/**
 * Broadcast receiver to handle device boot and restart services
 */
class BootReceiver : BroadcastReceiver() {
    
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_BOOT_COMPLETED,
            Intent.ACTION_MY_PACKAGE_REPLACED,
            Intent.ACTION_PACKAGE_REPLACED -> {
                Timber.d("Device booted or app updated, checking if services should be started")
                
                val preferenceManager = PreferenceManager(context)
                val config = preferenceManager.getForwardingConfig()
                
                if (config.isEnabled) {
                    // Start the SMS forwarding service
                    val serviceIntent = Intent(context, SMSForwardingService::class.java).apply {
                        action = SMSForwardingService.ACTION_START_FORWARDING
                    }
                    context.startForegroundService(serviceIntent)
                    
                    Timber.d("SMS forwarding service started on boot")
                }
            }
        }
    }
}
