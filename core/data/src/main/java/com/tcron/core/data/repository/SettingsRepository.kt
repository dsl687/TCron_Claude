package com.tcron.core.data.repository

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs: SharedPreferences = context.getSharedPreferences("tcron_settings", Context.MODE_PRIVATE)
    
    // App Settings
    private val _startOnBoot = MutableStateFlow(getStartOnBoot())
    val startOnBoot: StateFlow<Boolean> = _startOnBoot.asStateFlow()
    
    private val _autoStartTasks = MutableStateFlow(getAutoStartTasks())
    val autoStartTasks: StateFlow<Boolean> = _autoStartTasks.asStateFlow()
    
    private val _enableNotifications = MutableStateFlow(getEnableNotifications())
    val enableNotifications: StateFlow<Boolean> = _enableNotifications.asStateFlow()
    
    // Notification Settings
    private val _notificationSound = MutableStateFlow(getNotificationSound())
    val notificationSound: StateFlow<Boolean> = _notificationSound.asStateFlow()
    
    private val _notificationVibration = MutableStateFlow(getNotificationVibration())
    val notificationVibration: StateFlow<Boolean> = _notificationVibration.asStateFlow()
    
    private val _taskCompletionNotifs = MutableStateFlow(getTaskCompletionNotifs())
    val taskCompletionNotifs: StateFlow<Boolean> = _taskCompletionNotifs.asStateFlow()
    
    private val _taskFailureNotifs = MutableStateFlow(getTaskFailureNotifs())
    val taskFailureNotifs: StateFlow<Boolean> = _taskFailureNotifs.asStateFlow()
    
    private val _systemAlertsNotifs = MutableStateFlow(getSystemAlertsNotifs())
    val systemAlertsNotifs: StateFlow<Boolean> = _systemAlertsNotifs.asStateFlow()
    
    private val _selectedNotificationChannel = MutableStateFlow(getSelectedNotificationChannel())
    val selectedNotificationChannel: StateFlow<String> = _selectedNotificationChannel.asStateFlow()
    
    // System Settings
    private val _useRootPermissions = MutableStateFlow(getUseRootPermissions())
    val useRootPermissions: StateFlow<Boolean> = _useRootPermissions.asStateFlow()
    
    // Security Settings
    private val _requireBiometricAuth = MutableStateFlow(getRequireBiometricAuth())
    val requireBiometricAuth: StateFlow<Boolean> = _requireBiometricAuth.asStateFlow()
    
    private val _encryptScripts = MutableStateFlow(getEncryptScripts())
    val encryptScripts: StateFlow<Boolean> = _encryptScripts.asStateFlow()
    
    // App Settings
    fun setStartOnBoot(enabled: Boolean) {
        prefs.edit().putBoolean("start_on_boot", enabled).apply()
        _startOnBoot.value = enabled
    }
    
    fun setAutoStartTasks(enabled: Boolean) {
        prefs.edit().putBoolean("auto_start_tasks", enabled).apply()
        _autoStartTasks.value = enabled
    }
    
    fun setEnableNotifications(enabled: Boolean) {
        prefs.edit().putBoolean("enable_notifications", enabled).apply()
        _enableNotifications.value = enabled
    }
    
    // Notification Settings
    fun setNotificationSound(enabled: Boolean) {
        prefs.edit().putBoolean("notification_sound", enabled).apply()
        _notificationSound.value = enabled
    }
    
    fun setNotificationVibration(enabled: Boolean) {
        prefs.edit().putBoolean("notification_vibration", enabled).apply()
        _notificationVibration.value = enabled
    }
    
    fun setTaskCompletionNotifs(enabled: Boolean) {
        prefs.edit().putBoolean("task_completion_notifs", enabled).apply()
        _taskCompletionNotifs.value = enabled
    }
    
    fun setTaskFailureNotifs(enabled: Boolean) {
        prefs.edit().putBoolean("task_failure_notifs", enabled).apply()
        _taskFailureNotifs.value = enabled
    }
    
    fun setSystemAlertsNotifs(enabled: Boolean) {
        prefs.edit().putBoolean("system_alerts_notifs", enabled).apply()
        _systemAlertsNotifs.value = enabled
    }
    
    fun setSelectedNotificationChannel(channel: String) {
        prefs.edit().putString("selected_notification_channel", channel).apply()
        _selectedNotificationChannel.value = channel
    }
    
    // System Settings
    fun setUseRootPermissions(enabled: Boolean) {
        prefs.edit().putBoolean("use_root_permissions", enabled).apply()
        _useRootPermissions.value = enabled
    }
    
    // Security Settings
    fun setRequireBiometricAuth(enabled: Boolean) {
        prefs.edit().putBoolean("require_biometric_auth", enabled).apply()
        _requireBiometricAuth.value = enabled
    }
    
    fun setEncryptScripts(enabled: Boolean) {
        prefs.edit().putBoolean("encrypt_scripts", enabled).apply()
        _encryptScripts.value = enabled
    }
    
    // Private getters for initial values
    private fun getStartOnBoot(): Boolean = prefs.getBoolean("start_on_boot", false)
    private fun getAutoStartTasks(): Boolean = prefs.getBoolean("auto_start_tasks", true)
    private fun getEnableNotifications(): Boolean = prefs.getBoolean("enable_notifications", true)
    
    private fun getNotificationSound(): Boolean = prefs.getBoolean("notification_sound", true)
    private fun getNotificationVibration(): Boolean = prefs.getBoolean("notification_vibration", true)
    private fun getTaskCompletionNotifs(): Boolean = prefs.getBoolean("task_completion_notifs", true)
    private fun getTaskFailureNotifs(): Boolean = prefs.getBoolean("task_failure_notifs", true)
    private fun getSystemAlertsNotifs(): Boolean = prefs.getBoolean("system_alerts_notifs", false)
    private fun getSelectedNotificationChannel(): String = prefs.getString("selected_notification_channel", "Execução de Tarefas") ?: "Execução de Tarefas"
    
    private fun getUseRootPermissions(): Boolean = prefs.getBoolean("use_root_permissions", false)
    
    private fun getRequireBiometricAuth(): Boolean = prefs.getBoolean("require_biometric_auth", false)
    private fun getEncryptScripts(): Boolean = prefs.getBoolean("encrypt_scripts", true)
}