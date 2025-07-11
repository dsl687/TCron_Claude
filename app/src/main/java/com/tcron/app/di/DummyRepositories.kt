package com.tcron.app.di

import com.tcron.core.common.Result
import com.tcron.core.domain.model.*
import com.tcron.core.domain.repository.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

// Dummy implementations for repositories to make the app compile
// These will be replaced with actual implementations later

class DummyTaskRepository : TaskRepository {
    override fun getAllTasks(): Flow<List<Task>> = flowOf(emptyList())
    override fun getTaskById(taskId: String): Flow<Task?> = flowOf(null)
    override fun getTasksByType(type: TaskType): Flow<List<Task>> = flowOf(emptyList())
    override fun getEnabledTasks(): Flow<List<Task>> = flowOf(emptyList())
    override fun getScheduledTasks(): Flow<List<Task>> = flowOf(emptyList())
    override suspend fun insertTask(task: Task): Result<String> = Result.Success(task.id)
    override suspend fun updateTask(task: Task): Result<Unit> = Result.Success(Unit)
    override suspend fun deleteTask(taskId: String): Result<Unit> = Result.Success(Unit)
    override suspend fun toggleTaskEnabled(taskId: String, enabled: Boolean): Result<Unit> = Result.Success(Unit)
    override suspend fun executeTask(taskId: String): Result<TaskExecutionResult> = Result.Error(Exception("Not implemented"))
    override suspend fun cancelTaskExecution(taskId: String): Result<Unit> = Result.Success(Unit)
    override suspend fun getTaskExecutionHistory(taskId: String): Result<List<TaskExecutionResult>> = Result.Success(emptyList())
    override suspend fun getTaskExecutionResult(executionId: String): Result<TaskExecutionResult?> = Result.Success(null)
    override suspend fun exportTasks(format: String): Result<String> = Result.Success("")
    override suspend fun importTasks(data: String, format: String): Result<List<Task>> = Result.Success(emptyList())
    override suspend fun clearTaskHistory(taskId: String): Result<Unit> = Result.Success(Unit)
    override suspend fun getTaskStatus(taskId: String): Result<TaskStatus> = Result.Success(TaskStatus.PENDING)
    override suspend fun scheduleTask(task: Task): Result<Unit> = Result.Success(Unit)
    override suspend fun cancelScheduledTask(taskId: String): Result<Unit> = Result.Success(Unit)
    override suspend fun getRunningTasks(): Result<List<Task>> = Result.Success(emptyList())
}

class DummyTerminalRepository : TerminalRepository {
    override fun getActiveSessions(): Flow<List<TerminalSession>> = flowOf(emptyList())
    override fun getSessionById(sessionId: String): Flow<TerminalSession?> = flowOf(null)
    override suspend fun createSession(name: String, workingDirectory: String): Result<TerminalSession> = Result.Error(Exception("Not implemented"))
    override suspend fun closeSession(sessionId: String): Result<Unit> = Result.Success(Unit)
    override suspend fun executeCommand(sessionId: String, command: String): Result<TerminalCommand> = Result.Error(Exception("Not implemented"))
    override suspend fun executeCommandWithRoot(sessionId: String, command: String): Result<TerminalCommand> = Result.Error(Exception("Not implemented"))
    override suspend fun getCommandHistory(sessionId: String): Result<List<TerminalCommand>> = Result.Success(emptyList())
    override suspend fun getSessionOutput(sessionId: String): Result<List<TerminalOutput>> = Result.Success(emptyList())
    override suspend fun clearSessionHistory(sessionId: String): Result<Unit> = Result.Success(Unit)
    override suspend fun clearSessionOutput(sessionId: String): Result<Unit> = Result.Success(Unit)
    override suspend fun saveSessionAsScript(sessionId: String, fileName: String): Result<String> = Result.Success("")
    override suspend fun changeWorkingDirectory(sessionId: String, directory: String): Result<Unit> = Result.Success(Unit)
    override suspend fun getEnvironmentVariables(sessionId: String): Result<Map<String, String>> = Result.Success(emptyMap())
    override suspend fun setEnvironmentVariable(sessionId: String, key: String, value: String): Result<Unit> = Result.Success(Unit)
    override suspend fun getTerminalConfig(): Result<TerminalConfig> = Result.Error(Exception("Not implemented"))
    override suspend fun updateTerminalConfig(config: TerminalConfig): Result<Unit> = Result.Success(Unit)
    override suspend fun isRootAvailable(): Result<Boolean> = Result.Success(false)
    override suspend fun requestRootPermission(): Result<Boolean> = Result.Success(false)
    override suspend fun killProcess(sessionId: String): Result<Unit> = Result.Success(Unit)
    override suspend fun getProcessList(): Result<List<String>> = Result.Success(emptyList())
    override suspend fun exportSession(sessionId: String, format: String): Result<String> = Result.Success("")
}

class DummySettingsRepository : SettingsRepository {
    override fun getAppSettings(): Flow<AppSettings> = flowOf(
        AppSettings(
            theme = AppTheme.SYSTEM,
            accentColor = "#FF6200EE",
            language = "en",
            biometricEnabled = false,
            encryptionEnabled = false,
            autoStartEnabled = false,
            baseDirectory = "/sdcard/TCron",
            terminalConfig = TerminalConfig(
                fontSize = 14f,
                fontFamily = "monospace",
                backgroundColor = "#FF000000",
                foregroundColor = "#FFFFFFFF",
                cursorColor = "#FF00FF00",
                enableColors = true,
                enableBell = true,
                scrollbackLines = 1000,
                tabSize = 4
            ),
            notificationSettings = NotificationSettings(
                enabled = true,
                showTaskComplete = true,
                showTaskFailed = true,
                showTaskStarted = false,
                enableSound = true,
                enableVibration = true,
                priority = NotificationPriority.NORMAL
            ),
            dashboardSettings = DashboardSettings(
                refreshInterval = 5000L,
                showRealTimeCharts = true,
                maxDataPoints = 100,
                enableCpuMonitoring = true,
                enableMemoryMonitoring = true,
                enableBatteryMonitoring = true
            )
        )
    )
    override suspend fun updateAppSettings(settings: AppSettings): Result<Unit> = Result.Success(Unit)
    override suspend fun resetToDefaults(): Result<Unit> = Result.Success(Unit)
    override suspend fun exportSettings(): Result<String> = Result.Success("")
    override suspend fun importSettings(data: String): Result<Unit> = Result.Success(Unit)
    override suspend fun getSecuritySettings(): Result<SecuritySettings> = Result.Error(Exception("Not implemented"))
    override suspend fun updateSecuritySettings(settings: SecuritySettings): Result<Unit> = Result.Success(Unit)
    override suspend fun isBiometricEnabled(): Result<Boolean> = Result.Success(false)
    override suspend fun setBiometricEnabled(enabled: Boolean): Result<Unit> = Result.Success(Unit)
    override suspend fun isEncryptionEnabled(): Result<Boolean> = Result.Success(false)
    override suspend fun setEncryptionEnabled(enabled: Boolean): Result<Unit> = Result.Success(Unit)
    override suspend fun getBaseDirectory(): Result<String> = Result.Success("/sdcard/TCron")
    override suspend fun setBaseDirectory(directory: String): Result<Unit> = Result.Success(Unit)
    override suspend fun validateDirectory(directory: String): Result<Boolean> = Result.Success(true)
    override suspend fun createDirectoryStructure(baseDirectory: String): Result<Unit> = Result.Success(Unit)
    override suspend fun getTheme(): Result<String> = Result.Success("system")
    override suspend fun setTheme(theme: String): Result<Unit> = Result.Success(Unit)
    override suspend fun getAccentColor(): Result<String> = Result.Success("#FF6200EE")
    override suspend fun setAccentColor(color: String): Result<Unit> = Result.Success(Unit)
    override suspend fun getLanguage(): Result<String> = Result.Success("en")
    override suspend fun setLanguage(language: String): Result<Unit> = Result.Success(Unit)
    override suspend fun isAutoStartEnabled(): Result<Boolean> = Result.Success(false)
    override suspend fun setAutoStartEnabled(enabled: Boolean): Result<Unit> = Result.Success(Unit)
}

class DummyNotificationRepository : NotificationRepository {
    override fun getAllNotifications(): Flow<List<AppNotification>> = flowOf(emptyList())
    override fun getUnreadNotifications(): Flow<List<AppNotification>> = flowOf(emptyList())
    override fun getNotificationSummary(): Flow<NotificationSummary> = flowOf(
        NotificationSummary(0, 0, 0, 0, 0)
    )
    override suspend fun createNotification(title: String, message: String, type: NotificationType, taskId: String?): Result<AppNotification> = Result.Error(Exception("Not implemented"))
    override suspend fun markAsRead(notificationId: String): Result<Unit> = Result.Success(Unit)
    override suspend fun markAllAsRead(): Result<Unit> = Result.Success(Unit)
    override suspend fun deleteNotification(notificationId: String): Result<Unit> = Result.Success(Unit)
    override suspend fun deleteAllNotifications(): Result<Unit> = Result.Success(Unit)
    override suspend fun deleteReadNotifications(): Result<Unit> = Result.Success(Unit)
    override suspend fun getNotificationById(notificationId: String): Result<AppNotification?> = Result.Success(null)
    override suspend fun getNotificationsByType(type: NotificationType): Result<List<AppNotification>> = Result.Success(emptyList())
    override suspend fun getNotificationsByTask(taskId: String): Result<List<AppNotification>> = Result.Success(emptyList())
    override suspend fun showSystemNotification(notification: AppNotification): Result<Unit> = Result.Success(Unit)
    override suspend fun cancelSystemNotification(notificationId: String): Result<Unit> = Result.Success(Unit)
    override suspend fun createNotificationChannel(): Result<Unit> = Result.Success(Unit)
    override suspend fun updateNotificationSettings(): Result<Unit> = Result.Success(Unit)
}

class DummyDashboardRepository : DashboardRepository {
    override fun getDashboardMetrics(): Flow<DashboardMetrics> = flowOf(
        DashboardMetrics(0, 0, 0, 0, 0L, 0L, 0f, java.util.Date())
    )
    override fun getSystemMetrics(): Flow<SystemMetrics> = flowOf(
        SystemMetrics(java.util.Date(), 0f, 0L, 0L, 0f, 0f, 0L, 0L)
    )
    override fun getTaskMetrics(): Flow<List<TaskMetrics>> = flowOf(emptyList())
    override fun getPerformanceData(): Flow<List<PerformanceData>> = flowOf(emptyList())
    override suspend fun refreshMetrics(): Result<Unit> = Result.Success(Unit)
    override suspend fun getTaskExecutionChart(): Result<ChartData> = Result.Error(Exception("Not implemented"))
    override suspend fun getSuccessRateChart(): Result<ChartData> = Result.Error(Exception("Not implemented"))
    override suspend fun getCpuUsageChart(): Result<ChartData> = Result.Error(Exception("Not implemented"))
    override suspend fun getMemoryUsageChart(): Result<ChartData> = Result.Error(Exception("Not implemented"))
    override suspend fun getBatteryUsageChart(): Result<ChartData> = Result.Error(Exception("Not implemented"))
    override suspend fun getExecutionTimeChart(): Result<ChartData> = Result.Error(Exception("Not implemented"))
    override suspend fun getTaskTypeDistribution(): Result<ChartData> = Result.Error(Exception("Not implemented"))
    override suspend fun getFailureReasonChart(): Result<ChartData> = Result.Error(Exception("Not implemented"))
    override suspend fun getCurrentSystemStatus(): Result<SystemMetrics> = Result.Error(Exception("Not implemented"))
    override suspend fun getSystemMetricsHistory(hours: Int): Result<List<SystemMetrics>> = Result.Success(emptyList())
    override suspend fun getTaskPerformanceHistory(taskId: String): Result<List<PerformanceData>> = Result.Success(emptyList())
    override suspend fun exportDashboardData(format: String): Result<String> = Result.Success("")
    override suspend fun clearMetricsHistory(): Result<Unit> = Result.Success(Unit)
    override suspend fun getResourceUsageByTask(): Result<Map<String, PerformanceData>> = Result.Success(emptyMap())
    override suspend fun getTaskExecutionTrends(): Result<Map<String, List<Float>>> = Result.Success(emptyMap())
    override suspend fun getSystemHealthScore(): Result<Float> = Result.Success(0f)
    override suspend fun getPredictedBatteryDrain(): Result<Float> = Result.Success(0f)
    override suspend fun getOptimizationSuggestions(): Result<List<String>> = Result.Success(emptyList())
}