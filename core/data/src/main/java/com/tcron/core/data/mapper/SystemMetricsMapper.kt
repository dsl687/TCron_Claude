package com.tcron.core.data.mapper

import com.tcron.core.data.database.entity.SystemMetricsEntity
import com.tcron.core.domain.model.SystemMetrics

fun SystemMetricsEntity.toDomainModel(): SystemMetrics {
    return SystemMetrics(
        timestamp = timestamp,
        cpuUsage = cpuUsage,
        memoryUsage = memoryUsage,
        totalMemory = totalMemory,
        batteryLevel = batteryLevel,
        batteryTemperature = batteryTemperature,
        diskUsage = diskUsage,
        totalDisk = totalDisk
    )
}

fun SystemMetrics.toEntity(id: String): SystemMetricsEntity {
    return SystemMetricsEntity(
        id = id,
        timestamp = timestamp,
        cpuUsage = cpuUsage,
        memoryUsage = memoryUsage,
        totalMemory = totalMemory,
        batteryLevel = batteryLevel,
        batteryTemperature = batteryTemperature,
        diskUsage = diskUsage,
        totalDisk = totalDisk
    )
}