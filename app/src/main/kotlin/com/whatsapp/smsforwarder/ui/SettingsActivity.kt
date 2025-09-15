package com.whatsapp.smsforwarder.ui

import android.os.Bundle
import android.text.InputType
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.whatsapp.smsforwarder.R
import com.whatsapp.smsforwarder.databinding.ActivitySettingsBinding
import com.whatsapp.smsforwarder.util.SMSManager
import timber.log.Timber

class SettingsActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var viewModel: SettingsViewModel
    private lateinit var smsManager: SMSManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupViewModel()
        setupUI()
        loadSettings()
    }
    
    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[SettingsViewModel::class.java]
        smsManager = SMSManager(this)
    }
    
    private fun setupUI() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Settings"
        
        // Set input type for phone number
        binding.etPhoneNumber.inputType = InputType.TYPE_CLASS_PHONE
        
        binding.btnSave.setOnClickListener {
            saveSettings()
        }
        
        binding.btnTestSMS.setOnClickListener {
            testSMS()
        }
        
        // Observe ViewModel state
        viewModel.forwardingConfig.observe(this) { config ->
            updateUI(config)
        }
        
        viewModel.saveResult.observe(this) { result ->
            when (result) {
                is com.whatsapp.smsforwarder.data.Result.Success -> {
                    Toast.makeText(this, "Settings saved successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }
                is com.whatsapp.smsforwarder.data.Result.Error -> {
                    Toast.makeText(this, "Error saving settings: ${result.exception.message}", Toast.LENGTH_LONG).show()
                }
                else -> {
                    // Loading state
                }
            }
        }
        
        viewModel.testSMSResult.observe(this) { result ->
            when (result) {
                is com.whatsapp.smsforwarder.data.Result.Success -> {
                    Toast.makeText(this, "Test SMS sent successfully", Toast.LENGTH_SHORT).show()
                }
                is com.whatsapp.smsforwarder.data.Result.Error -> {
                    Toast.makeText(this, "Failed to send test SMS: ${result.exception.message}", Toast.LENGTH_LONG).show()
                }
                else -> {
                    // Loading state
                }
            }
        }
    }
    
    private fun loadSettings() {
        viewModel.loadSettings()
    }
    
    private fun updateUI(config: com.whatsapp.smsforwarder.data.ForwardingConfig) {
        binding.etPhoneNumber.setText(config.targetPhoneNumber)
        binding.switchIncludeSender.isChecked = config.includeSender
        binding.switchIncludeTimestamp.isChecked = config.includeTimestamp
        binding.etCustomPrefix.setText(config.customPrefix)
    }
    
    private fun saveSettings() {
        val phoneNumber = binding.etPhoneNumber.text.toString().trim()
        
        if (phoneNumber.isEmpty()) {
            binding.etPhoneNumber.error = "Phone number is required"
            return
        }
        
        if (!smsManager.isValidPhoneNumber(phoneNumber)) {
            binding.etPhoneNumber.error = "Invalid phone number format"
            return
        }
        
        val config = com.whatsapp.smsforwarder.data.ForwardingConfig(
            targetPhoneNumber = phoneNumber,
            isEnabled = true, // Will be set by main activity
            includeSender = binding.switchIncludeSender.isChecked,
            includeTimestamp = binding.switchIncludeTimestamp.isChecked,
            customPrefix = binding.etCustomPrefix.text.toString().trim().ifEmpty { "WhatsApp: " }
        )
        
        viewModel.saveSettings(config)
    }
    
    private fun testSMS() {
        val phoneNumber = binding.etPhoneNumber.text.toString().trim()
        
        if (phoneNumber.isEmpty()) {
            binding.etPhoneNumber.error = "Phone number is required"
            return
        }
        
        if (!smsManager.isValidPhoneNumber(phoneNumber)) {
            binding.etPhoneNumber.error = "Invalid phone number format"
            return
        }
        
        val testMessage = com.whatsapp.smsforwarder.data.WhatsAppMessage(
            sender = "Test User",
            message = "This is a test message from WhatsApp SMS Forwarder",
            timestamp = System.currentTimeMillis()
        )
        
        val config = com.whatsapp.smsforwarder.data.ForwardingConfig(
            targetPhoneNumber = phoneNumber,
            isEnabled = true,
            includeSender = binding.switchIncludeSender.isChecked,
            includeTimestamp = binding.switchIncludeTimestamp.isChecked,
            customPrefix = binding.etCustomPrefix.text.toString().trim().ifEmpty { "WhatsApp: " }
        )
        
        viewModel.testSMS(testMessage, config)
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
