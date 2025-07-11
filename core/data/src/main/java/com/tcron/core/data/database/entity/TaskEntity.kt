package com.tcron.core.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.tcron.core.data.database.converter.DateConverter
import com.tcron.core.data.database.converter.TaskTypeConverter
import com.tcron.core.domain.model.TaskType
import java.util.Date

@Entity(tableName = "tasks")
@TypeConverters(DateConverter::class, TaskTypeConverter::class)
data class TaskEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String,
    val type: TaskType,
    val scriptContent: String,
    val requiresRoot: Boolean,
    val requiresNetwork: Boolean,
    val requiresStorage: Boolean,
    val customPermissions: String, // JSON string
    val scheduledTime: Date?,
    val repeatType: String,
    val repeatInterval: Long,
    val isOneTime: Boolean,
    val executeOnBoot: Boolean,
    val delayAfterBoot: Long,
    val maxExecutionTime: Long,
    val isEnabled: Boolean,
    val lastExecutionTime: Date?,
    val lastExecutionResult: String?, // JSON string
    val executionCount: Int,
    val successCount: Int,
    val failureCount: Int,
    val averageExecutionTime: Long,
    val createdAt: Date,
    val updatedAt: Date
)