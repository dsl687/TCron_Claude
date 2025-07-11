package com.tcron.core.data.mapper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tcron.core.data.database.entity.TaskEntity
import com.tcron.core.domain.model.RepeatType
import com.tcron.core.domain.model.Task
import com.tcron.core.domain.model.TaskExecutionResult
import com.tcron.core.domain.model.TaskPermissions
import com.tcron.core.domain.model.TaskSchedule

private val gson = Gson()

fun TaskEntity.toDomainModel(): Task {
    val permissions = TaskPermissions(
        requiresRoot = requiresRoot,
        requiresNetwork = requiresNetwork,
        requiresStorage = requiresStorage,
        customPermissions = if (customPermissions.isNotEmpty()) {
            gson.fromJson(customPermissions, object : TypeToken<List<String>>() {}.type)
        } else {
            emptyList()
        }
    )
    
    val schedule = if (scheduledTime != null) {
        TaskSchedule(
            scheduledTime = scheduledTime,
            repeatType = RepeatType.valueOf(repeatType),
            repeatInterval = repeatInterval,
            isOneTime = isOneTime,
            executeOnBoot = executeOnBoot,
            delayAfterBoot = delayAfterBoot,
            maxExecutionTime = maxExecutionTime
        )
    } else null
    
    val executionResult = if (lastExecutionResult != null) {
        gson.fromJson(lastExecutionResult, TaskExecutionResult::class.java)
    } else null
    
    return Task(
        id = id,
        name = name,
        description = description,
        type = type,
        scriptContent = scriptContent,
        permissions = permissions,
        schedule = schedule,
        isEnabled = isEnabled,
        lastExecutionTime = lastExecutionTime,
        lastExecutionResult = executionResult,
        executionCount = executionCount,
        successCount = successCount,
        failureCount = failureCount,
        averageExecutionTime = averageExecutionTime,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun Task.toEntity(): TaskEntity {
    val customPermissionsJson = if (permissions.customPermissions.isNotEmpty()) {
        gson.toJson(permissions.customPermissions)
    } else ""
    
    val executionResultJson = lastExecutionResult?.let { gson.toJson(it) }
    
    return TaskEntity(
        id = id,
        name = name,
        description = description,
        type = type,
        scriptContent = scriptContent,
        requiresRoot = permissions.requiresRoot,
        requiresNetwork = permissions.requiresNetwork,
        requiresStorage = permissions.requiresStorage,
        customPermissions = customPermissionsJson,
        scheduledTime = schedule?.scheduledTime,
        repeatType = schedule?.repeatType?.name ?: RepeatType.NONE.name,
        repeatInterval = schedule?.repeatInterval ?: 0L,
        isOneTime = schedule?.isOneTime ?: true,
        executeOnBoot = schedule?.executeOnBoot ?: false,
        delayAfterBoot = schedule?.delayAfterBoot ?: 0L,
        maxExecutionTime = schedule?.maxExecutionTime ?: 30000L,
        isEnabled = isEnabled,
        lastExecutionTime = lastExecutionTime,
        lastExecutionResult = executionResultJson,
        executionCount = executionCount,
        successCount = successCount,
        failureCount = failureCount,
        averageExecutionTime = averageExecutionTime,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}