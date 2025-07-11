package com.tcron.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class DashboardMetrics(
    val totalTasks: Int,
    val activeTasks: Int,
    val completedTasks: Int,
    val failedTasks: Int,
    val averageExecutionTime: Long,
    val totalExecutionTime: Long,
    val successRate: Float,
    val lastUpdateTime: Date
) : Parcelable

@Parcelize
data class SystemMetrics(
    val timestamp: Date,
    val cpuUsage: Float,
    val memoryUsage: Long,
    val totalMemory: Long,
    val batteryLevel: Float,
    val batteryTemperature: Float,
    val diskUsage: Long,
    val totalDisk: Long
) : Parcelable

@Parcelize
data class TaskMetrics(
    val taskId: String,
    val taskName: String,
    val executionCount: Int,
    val successCount: Int,
    val failureCount: Int,
    val averageExecutionTime: Long,
    val lastExecutionTime: Date?,
    val lastExecutionResult: TaskExecutionResult?
) : Parcelable

@Parcelize
data class ChartData(
    val labels: List<String>,
    val values: List<Float>,
    val colors: List<String>,
    val type: ChartType
) : Parcelable

@Parcelize
enum class ChartType : Parcelable {
    LINE,
    BAR,
    PIE,
    AREA
}

@Parcelize
data class PerformanceData(
    val timestamp: Date,
    val cpuUsage: Float,
    val memoryUsage: Float,
    val batteryUsage: Float,
    val activeTasksCount: Int
) : Parcelable