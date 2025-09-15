package com.whatsapp.smsforwarder.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import timber.log.Timber

/**
 * Helper class for managing app permissions
 */
object PermissionHelper {
    
    const val SMS_PERMISSION_REQUEST_CODE = 1001
    
    private val SMS_PERMISSIONS = arrayOf(
        Manifest.permission.SEND_SMS,
        Manifest.permission.RECEIVE_SMS,
        Manifest.permission.READ_SMS
    )
    
    /**
     * Check if all SMS permissions are granted
     */
    fun hasSMSPermissions(context: Context): Boolean {
        return SMS_PERMISSIONS.all { permission ->
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        }
    }
    
    /**
     * Request SMS permissions
     */
    fun requestSMSPermissions(activity: Activity) {
        val permissionsToRequest = SMS_PERMISSIONS.filter { permission ->
            ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED
        }
        
        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                activity,
                permissionsToRequest.toTypedArray(),
                SMS_PERMISSION_REQUEST_CODE
            )
            Timber.d("Requesting SMS permissions: ${permissionsToRequest.joinToString()}")
        } else {
            Timber.d("All SMS permissions already granted")
        }
    }
    
    /**
     * Handle permission request result
     */
    fun handlePermissionResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ): Boolean {
        if (requestCode == SMS_PERMISSION_REQUEST_CODE) {
            val allGranted = grantResults.all { it == PackageManager.PERMISSION_GRANTED }
            Timber.d("SMS permissions granted: $allGranted")
            return allGranted
        }
        return false
    }
    
    /**
     * Check if notification access is enabled
     */
    fun isNotificationAccessEnabled(context: Context): Boolean {
        val pkgName = context.packageName
        val flat = android.provider.Settings.Secure.getString(
            context.contentResolver,
            "enabled_notification_listeners"
        )
        return flat?.contains(pkgName) == true
    }
}
