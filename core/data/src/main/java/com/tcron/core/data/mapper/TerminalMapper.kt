package com.tcron.core.data.mapper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tcron.core.data.database.entity.TerminalCommandEntity
import com.tcron.core.data.database.entity.TerminalSessionEntity
import com.tcron.core.domain.model.TerminalCommand
import com.tcron.core.domain.model.TerminalSession

private val gson = Gson()

fun TerminalSessionEntity.toDomainModel(commands: List<TerminalCommand> = emptyList()): TerminalSession {
    val environmentMap = if (environment.isNotEmpty()) {
        gson.fromJson<Map<String, String>>(
            environment,
            object : TypeToken<Map<String, String>>() {}.type
        )
    } else {
        emptyMap()
    }
    
    return TerminalSession(
        id = id,
        name = name,
        isActive = isActive,
        workingDirectory = workingDirectory,
        environment = environmentMap,
        history = commands,
        output = output,
        createdAt = createdAt,
        lastUsedAt = lastUsedAt
    )
}

fun TerminalSession.toEntity(): TerminalSessionEntity {
    val environmentJson = if (environment.isNotEmpty()) {
        gson.toJson(environment)
    } else ""
    
    return TerminalSessionEntity(
        id = id,
        name = name,
        isActive = isActive,
        workingDirectory = workingDirectory,
        environment = environmentJson,
        output = output,
        createdAt = createdAt,
        lastUsedAt = lastUsedAt
    )
}

fun TerminalCommandEntity.toDomainModel(): TerminalCommand {
    return TerminalCommand(
        id = id,
        command = command,
        workingDirectory = workingDirectory,
        exitCode = exitCode,
        output = output,
        errorOutput = errorOutput,
        executionTime = executionTime,
        timestamp = timestamp,
        isSuccess = isSuccess
    )
}

fun TerminalCommand.toEntity(sessionId: String): TerminalCommandEntity {
    return TerminalCommandEntity(
        id = id,
        sessionId = sessionId,
        command = command,
        workingDirectory = workingDirectory,
        exitCode = exitCode,
        output = output,
        errorOutput = errorOutput,
        executionTime = executionTime,
        timestamp = timestamp,
        isSuccess = isSuccess
    )
}