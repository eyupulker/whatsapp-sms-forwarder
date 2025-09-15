package com.whatsapp.smsforwarder.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.whatsapp.smsforwarder.R
import com.whatsapp.smsforwarder.databinding.ActivityMainBinding
import com.whatsapp.smsforwarder.util.PermissionHelper
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupViewModel()
        setupUI()
        checkPermissions()
    }
    
    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
    }
    
    private fun setupUI() {
        setSupportActionBar(binding.toolbar)
        
        binding.btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
        
        binding.btnGrantNotificationAccess.setOnClickListener {
            openNotificationAccessSettings()
        }
        
        binding.btnGrantSMSPermission.setOnClickListener {
            PermissionHelper.requestSMSPermissions(this)
        }
        
        // Observe ViewModel state
        viewModel.forwardingConfig.observe(this) { config ->
            updateUI(config)
        }
        
        viewModel.isNotificationAccessGranted.observe(this) { granted ->
            binding.layoutNotificationAccess.visibility = if (granted) android.view.View.GONE else android.view.View.VISIBLE
        }
        
        viewModel.isSMSPermissionGranted.observe(this) { granted ->
            binding.layoutSMSPermission.visibility = if (granted) android.view.View.GONE else android.view.View.VISIBLE
        }
    }
    
    private fun updateUI(config: com.whatsapp.smsforwarder.data.ForwardingConfig) {
        binding.tvStatus.text = if (config.isEnabled) {
            "Forwarding enabled to: ${config.targetPhoneNumber}"
        } else {
            "Forwarding disabled"
        }
        
        binding.switchForwarding.isChecked = config.isEnabled
        binding.switchForwarding.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setForwardingEnabled(isChecked)
        }
    }
    
    private fun checkPermissions() {
        viewModel.checkPermissions()
    }
    
    private fun openNotificationAccessSettings() {
        val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
        startActivity(intent)
    }
    
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            R.id.action_about -> {
                // Show about dialog
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    
    override fun onResume() {
        super.onResume()
        viewModel.checkPermissions()
    }
}
