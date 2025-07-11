package com.tcron.core.data.mapper

import com.tcron.core.data.database.entity.TaskExecutionEntity
import com.tcron.core.domain.model.TaskExecutionResult

fun TaskExecutionEntity.toDomainModel(): TaskExecutionResult {
    return TaskExecutionResult(
        id = id,
        taskId = taskId,
        startTime = startTime,
        endTime = endTime,
        exitCode = exitCode,
        output = output,
        errorOutput = errorOutput,
        isSuccess = isSuccess,
        executionTime = executionTime,
        cpuUsage = cpuUsage,
        memoryUsage = memoryUsage,
        batteryUsage = batteryUsage
    )
}

fun TaskExecutionResult.toEntity(): TaskExecutionEntity {
    return TaskExecutionEntity(
        id = id,
        taskId = taskId,
        startTime = startTime,
        endTime = endTime,
        exitCode = exitCode,
        output = output,
        errorOutput = errorOutput,
        isSuccess = isSuccess,
        executionTime = executionTime,
        cpuUsage = cpuUsage,
        memoryUsage = memoryUsage,
        batteryUsage = batteryUsage
    )
}