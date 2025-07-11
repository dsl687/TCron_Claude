package com.tcron.core.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.tcron.core.data.database.converter.DateConverter
import java.util.Date

@Entity(tableName = "system_metrics")
@TypeConverters(DateConverter::class)
data class SystemMetricsEntity(
    @PrimaryKey
    val id: String,
    val timestamp: Date,
    val cpuUsage: Float,
    val memoryUsage: Long,
    val totalMemory: Long,
    val batteryLevel: Float,
    val batteryTemperature: Float,
    val diskUsage: Long,
    val totalDisk: Long
)