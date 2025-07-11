package com.tcron.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AppSettings(
    val theme: AppTheme,
    val accentColor: String,
    val language: String,
    val biometricEnabled: Boolean,
    val encryptionEnabled: Boolean,
    val autoStartEnabled: Boolean,
    val baseDirectory: String,
    val terminalConfig: TerminalConfig,
    val notificationSettings: NotificationSettings,
    val dashboardSettings: DashboardSettings
) : Parcelable

@Parcelize
enum class AppTheme : Parcelable {
    LIGHT,
    DARK,
    AMOLED,
    SYSTEM
}

@Parcelize
data class NotificationSettings(
    val enabled: Boolean,
    val showTaskComplete: Boolean,
    val showTaskFailed: Boolean,
    val showTaskStarted: Boolean,
    val enableSound: Boolean,
    val enableVibration: Boolean,
    val priority: NotificationPriority
) : Parcelable

@Parcelize
enum class NotificationPriority : Parcelable {
    LOW,
    NORMAL,
    HIGH
}

@Parcelize
data class DashboardSettings(
    val refreshInterval: Long,
    val showRealTimeCharts: Boolean,
    val maxDataPoints: Int,
    val enableCpuMonitoring: Boolean,
    val enableMemoryMonitoring: Boolean,
    val enableBatteryMonitoring: Boolean
) : Parcelable

@Parcelize
data class SecuritySettings(
    val encryptionKey: String,
    val biometricTimeout: Long,
    val requireBiometricForSensitiveActions: Boolean,
    val enableRootPermissions: Boolean,
    val allowDangerousCommands: Boolean
) : Parcelable