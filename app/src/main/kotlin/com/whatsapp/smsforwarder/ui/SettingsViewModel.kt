package com.whatsapp.smsforwarder.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.whatsapp.smsforwarder.data.ForwardingConfig
import com.whatsapp.smsforwarder.data.Result
import com.whatsapp.smsforwarder.util.PreferenceManager
import com.whatsapp.smsforwarder.util.SMSManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    
    private val preferenceManager = PreferenceManager(application)
    private val smsManager = SMSManager(application)
    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    
    private val _forwardingConfig = MutableLiveData<ForwardingConfig>()
    val forwardingConfig: LiveData<ForwardingConfig> = _forwardingConfig
    
    private val _saveResult = MutableLiveData<Result<Unit>>()
    val saveResult: LiveData<Result<Unit>> = _saveResult
    
    private val _testSMSResult = MutableLiveData<Result<Unit>>()
    val testSMSResult: LiveData<Result<Unit>> = _testSMSResult
    
    fun loadSettings() {
        _forwardingConfig.value = preferenceManager.getForwardingConfig()
    }
    
    fun saveSettings(config: ForwardingConfig) {
        viewModelScope.launch {
            _saveResult.value = Result.Loading
            
            try {
                preferenceManager.saveForwardingConfig(config)
                _saveResult.value = Result.Success(Unit)
                Timber.d("Settings saved successfully")
            } catch (e: Exception) {
                _saveResult.value = Result.Error(e)
                Timber.e(e, "Error saving settings")
            }
        }
    }
    
    fun testSMS(testMessage: com.whatsapp.smsforwarder.data.WhatsAppMessage, config: ForwardingConfig) {
        viewModelScope.launch {
            _testSMSResult.value = Result.Loading
            
            val result = smsManager.forwardWhatsAppMessage(testMessage, config)
            _testSMSResult.value = result
            
            when (result) {
                is Result.Success -> {
                    Timber.d("Test SMS sent successfully")
                }
                is Result.Error -> {
                    Timber.e(result.exception, "Failed to send test SMS")
                }
                else -> {
                    Timber.w("Unexpected result when sending test SMS")
                }
            }
        }
    }
}
