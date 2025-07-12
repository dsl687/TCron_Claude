package com.tcron.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tcron.core.data.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val startOnBoot: Boolean = false,
    val autoStartTasks: Boolean = true,
    val enableNotifications: Boolean = true,
    val notificationSound: Boolean = true,
    val notificationVibration: Boolean = true,
    val taskCompletionNotifs: Boolean = true,
    val taskFailureNotifs: Boolean = true,
    val systemAlertsNotifs: Boolean = false,
    val selectedNotificationChannel: String = "Execução de Tarefas",
    val useRootPermissions: Boolean = false,
    val requireBiometricAuth: Boolean = false,
    val encryptScripts: Boolean = true,
    val rootStatus: String = "Toque em verificar para testar",
    val isCheckingRoot: Boolean = false
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()
    
    init {
        viewModelScope.launch {
            combine(
                settingsRepository.startOnBoot,
                settingsRepository.autoStartTasks,
                settingsRepository.enableNotifications,
                settingsRepository.notificationSound,
                settingsRepository.notificationVibration,
                settingsRepository.taskCompletionNotifs,
                settingsRepository.taskFailureNotifs,
                settingsRepository.systemAlertsNotifs,
                settingsRepository.selectedNotificationChannel,
                settingsRepository.useRootPermissions,
                settingsRepository.requireBiometricAuth,
                settingsRepository.encryptScripts
            ) { flows ->
                _uiState.value = _uiState.value.copy(
                    startOnBoot = flows[0] as Boolean,
                    autoStartTasks = flows[1] as Boolean,
                    enableNotifications = flows[2] as Boolean,
                    notificationSound = flows[3] as Boolean,
                    notificationVibration = flows[4] as Boolean,
                    taskCompletionNotifs = flows[5] as Boolean,
                    taskFailureNotifs = flows[6] as Boolean,
                    systemAlertsNotifs = flows[7] as Boolean,
                    selectedNotificationChannel = flows[8] as String,
                    useRootPermissions = flows[9] as Boolean,
                    requireBiometricAuth = flows[10] as Boolean,
                    encryptScripts = flows[11] as Boolean
                )
            }.collect { }
        }
    }
    
    fun setStartOnBoot(enabled: Boolean) {
        settingsRepository.setStartOnBoot(enabled)
    }
    
    fun setAutoStartTasks(enabled: Boolean) {
        settingsRepository.setAutoStartTasks(enabled)
    }
    
    fun setEnableNotifications(enabled: Boolean) {
        settingsRepository.setEnableNotifications(enabled)
    }
    
    fun setNotificationSound(enabled: Boolean) {
        settingsRepository.setNotificationSound(enabled)
    }
    
    fun setNotificationVibration(enabled: Boolean) {
        settingsRepository.setNotificationVibration(enabled)
    }
    
    fun setTaskCompletionNotifs(enabled: Boolean) {
        settingsRepository.setTaskCompletionNotifs(enabled)
    }
    
    fun setTaskFailureNotifs(enabled: Boolean) {
        settingsRepository.setTaskFailureNotifs(enabled)
    }
    
    fun setSystemAlertsNotifs(enabled: Boolean) {
        settingsRepository.setSystemAlertsNotifs(enabled)
    }
    
    fun setSelectedNotificationChannel(channel: String) {
        settingsRepository.setSelectedNotificationChannel(channel)
    }
    
    fun setUseRootPermissions(enabled: Boolean) {
        settingsRepository.setUseRootPermissions(enabled)
    }
    
    fun setRequireBiometricAuth(enabled: Boolean) {
        settingsRepository.setRequireBiometricAuth(enabled)
    }
    
    fun setEncryptScripts(enabled: Boolean) {
        settingsRepository.setEncryptScripts(enabled)
    }
    
    fun checkRootAccess() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isCheckingRoot = true)
            
            try {
                val isRooted = performRootCheck()
                _uiState.value = _uiState.value.copy(
                    rootStatus = if (isRooted) {
                        "✅ Acesso root disponível"
                    } else {
                        "❌ Sem acesso root"
                    },
                    isCheckingRoot = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    rootStatus = "⚠️ Erro na verificação: ${e.message}",
                    isCheckingRoot = false
                )
            }
        }
    }
    
    private suspend fun performRootCheck(): Boolean {
        return try {
            // Mesmo método de verificação do SettingsScreen
            val rootBinaries = listOf(
                "/system/app/Superuser.apk",
                "/sbin/su",
                "/system/bin/su",
                "/system/xbin/su",
                "/data/local/xbin/su",
                "/data/local/bin/su",
                "/system/sd/xbin/su",
                "/system/bin/failsafe/su",
                "/data/local/su",
                "/su/bin/su"
            )
            
            val rootBinaryExists = rootBinaries.any { path -> java.io.File(path).exists() }
            
            val suCommandWorks = try {
                val process = Runtime.getRuntime().exec(arrayOf("su", "-c", "id"))
                val exitCode = process.waitFor()
                val output = process.inputStream.bufferedReader().readText()
                exitCode == 0 && output.contains("uid=0")
            } catch (e: Exception) {
                false
            }
            
            val buildTags = System.getProperty("ro.build.tags") ?: ""
            val isTestBuild = buildTags.contains("test-keys")
            
            val processUid = android.os.Process.myUid()
            val isRootUid = processUid == 0
            
            rootBinaryExists || suCommandWorks || isTestBuild || isRootUid
        } catch (e: Exception) {
            false
        }
    }
}