package com.tcron.core.data.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.tcron.core.data.database.converter.DateConverter
import java.util.Date

@Entity(
    tableName = "task_executions",
    foreignKeys = [
        ForeignKey(
            entity = TaskEntity::class,
            parentColumns = ["id"],
            childColumns = ["taskId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
@TypeConverters(DateConverter::class)
data class TaskExecutionEntity(
    @PrimaryKey
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
)