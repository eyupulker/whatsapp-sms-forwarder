package com.whatsapp.smsforwarder

import android.app.Application
import timber.log.Timber

class WhatsAppSMSForwarderApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize Timber for logging
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            // In release builds, you might want to use a different tree
            // that logs to a file or remote service
            Timber.plant(object : Timber.Tree() {
                override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                    // Implement release logging here
                }
            })
        }
        
        Timber.d("WhatsApp SMS Forwarder Application started")
    }
}
