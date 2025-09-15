package com.whatsapp.smsforwarder.ui

import android.app.Application
import android.content.Context
import android.provider.Settings
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.whatsapp.smsforwarder.data.ForwardingConfig
import com.whatsapp.smsforwarder.util.PermissionHelper
import com.whatsapp.smsforwarder.util.PreferenceManager
import timber.log.Timber

class MainViewModel(application: Application) : AndroidViewModel(application) {
    
    private val preferenceManager = PreferenceManager(application)
    
    private val _forwardingConfig = MutableLiveData<ForwardingConfig>()
    val forwardingConfig: LiveData<ForwardingConfig> = _forwardingConfig
    
    private val _isNotificationAccessGranted = MutableLiveData<Boolean>()
    val isNotificationAccessGranted: LiveData<Boolean> = _isNotificationAccessGranted
    
    private val _isSMSPermissionGranted = MutableLiveData<Boolean>()
    val isSMSPermissionGranted: LiveData<Boolean> = _isSMSPermissionGranted
    
    init {
        loadForwardingConfig()
    }
    
    private fun loadForwardingConfig() {
        _forwardingConfig.value = preferenceManager.getForwardingConfig()
    }
    
    fun setForwardingEnabled(enabled: Boolean) {
        val currentConfig = _forwardingConfig.value ?: ForwardingConfig("")
        val newConfig = currentConfig.copy(isEnabled = enabled)
        preferenceManager.saveForwardingConfig(newConfig)
        _forwardingConfig.value = newConfig
        
        Timber.d("Forwarding enabled: $enabled")
    }
    
    fun checkPermissions() {
        val context = getApplication<Application>()
        
        // Check notification access
        val notificationAccessGranted = isNotificationAccessEnabled(context)
        _isNotificationAccessGranted.value = notificationAccessGranted
        preferenceManager.setNotificationAccessGranted(notificationAccessGranted)
        
        // Check SMS permission
        val smsPermissionGranted = PermissionHelper.hasSMSPermissions(context)
        _isSMSPermissionGranted.value = smsPermissionGranted
        preferenceManager.setSMSPermissionGranted(smsPermissionGranted)
        
        Timber.d("Permissions checked - Notification: $notificationAccessGranted, SMS: $smsPermissionGranted")
    }
    
    private fun isNotificationAccessEnabled(context: Context): Boolean {
        val pkgName = context.packageName
        val flat = Settings.Secure.getString(
            context.contentResolver,
            "enabled_notification_listeners"
        )
        return flat?.contains(pkgName) == true
    }
}
