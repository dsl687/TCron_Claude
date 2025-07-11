package com.tcron.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Task(
    val id: String,
    val name: String,
    val description: String,
    val type: TaskType,
    val scriptContent: String,
    val permissions: TaskPermissions,
    val schedule: TaskSchedule?,
    val isEnabled: Boolean,
    val lastExecutionTime: Date?,
    val lastExecutionResult: TaskExecutionResult?,
    val executionCount: Int,
    val successCount: Int,
    val failureCount: Int,
    val averageExecutionTime: Long,
    val createdAt: Date,
    val updatedAt: Date
) : Parcelable

@Parcelize
enum class TaskType : Parcelable {
    SHELL,
    PYTHON,
    COMBINED
}

@Parcelize
data class TaskPermissions(
    val requiresRoot: Boolean,
    val requiresNetwork: Boolean,
    val requiresStorage: Boolean,
    val customPermissions: List<String>
) : Parcelable

@Parcelize
data class TaskSchedule(
    val scheduledTime: Date,
    val repeatType: RepeatType,
    val repeatInterval: Long,
    val isOneTime: Boolean,
    val executeOnBoot: Boolean,
    val delayAfterBoot: Long,
    val maxExecutionTime: Long
) : Parcelable

@Parcelize
enum class RepeatType : Parcelable {
    NONE,
    DAILY,
    WEEKLY,
    MONTHLY,
    CUSTOM
}

@Parcelize
data class TaskExecutionResult(
    val id: String,
    val taskId: String,
    val startTime: Date,
    val endTime: Date,
    val exitCode: Int,
    val output: String,
    val errorOutput: String,
    val isSuccess: Boolean,
    val executionTime: Long,
    val cpuUsage: Float,
    val memoryUsage: Long,
    val batteryUsage: Float
) : Parcelable

@Parcelize
enum class TaskStatus : Parcelable {
    PENDING,
    RUNNING,
    COMPLETED,
    FAILED,
    CANCELLED,
    TIMEOUT
}