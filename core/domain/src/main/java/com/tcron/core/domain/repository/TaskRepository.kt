package com.tcron.core.domain.repository

import com.tcron.core.common.Result
import com.tcron.core.domain.model.Task
import com.tcron.core.domain.model.TaskExecutionResult
import com.tcron.core.domain.model.TaskStatus
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    
    fun getAllTasks(): Flow<List<Task>>
    
    fun getTaskById(taskId: String): Flow<Task?>
    
    fun getTasksByType(type: com.tcron.core.domain.model.TaskType): Flow<List<Task>>
    
    fun getEnabledTasks(): Flow<List<Task>>
    
    fun getScheduledTasks(): Flow<List<Task>>
    
    suspend fun insertTask(task: Task): Result<String>
    
    suspend fun updateTask(task: Task): Result<Unit>
    
    suspend fun deleteTask(taskId: String): Result<Unit>
    
    suspend fun toggleTaskEnabled(taskId: String, enabled: Boolean): Result<Unit>
    
    suspend fun executeTask(taskId: String): Result<TaskExecutionResult>
    
    suspend fun cancelTaskExecution(taskId: String): Result<Unit>
    
    suspend fun getTaskExecutionHistory(taskId: String): Result<List<TaskExecutionResult>>
    
    suspend fun getTaskExecutionResult(executionId: String): Result<TaskExecutionResult?>
    
    suspend fun exportTasks(format: String): Result<String>
    
    suspend fun importTasks(data: String, format: String): Result<List<Task>>
    
    suspend fun clearTaskHistory(taskId: String): Result<Unit>
    
    suspend fun getTaskStatus(taskId: String): Result<TaskStatus>
    
    suspend fun scheduleTask(task: Task): Result<Unit>
    
    suspend fun cancelScheduledTask(taskId: String): Result<Unit>
    
    suspend fun getRunningTasks(): Result<List<Task>>
}