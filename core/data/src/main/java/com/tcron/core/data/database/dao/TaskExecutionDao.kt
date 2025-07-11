package com.tcron.core.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tcron.core.data.database.entity.TaskExecutionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskExecutionDao {
    
    @Query("SELECT * FROM task_executions WHERE taskId = :taskId ORDER BY startTime DESC")
    fun getExecutionsByTaskId(taskId: String): Flow<List<TaskExecutionEntity>>
    
    @Query("SELECT * FROM task_executions WHERE id = :executionId")
    suspend fun getExecutionById(executionId: String): TaskExecutionEntity?
    
    @Query("SELECT * FROM task_executions ORDER BY startTime DESC LIMIT :limit")
    fun getRecentExecutions(limit: Int): Flow<List<TaskExecutionEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExecution(execution: TaskExecutionEntity)
    
    @Query("DELETE FROM task_executions WHERE taskId = :taskId")
    suspend fun deleteExecutionsByTaskId(taskId: String)
    
    @Query("DELETE FROM task_executions WHERE id = :executionId")
    suspend fun deleteExecutionById(executionId: String)
    
    @Query("SELECT COUNT(*) FROM task_executions WHERE taskId = :taskId")
    suspend fun getExecutionCountByTaskId(taskId: String): Int
    
    @Query("SELECT COUNT(*) FROM task_executions WHERE taskId = :taskId AND isSuccess = 1")
    suspend fun getSuccessCountByTaskId(taskId: String): Int
    
    @Query("SELECT COUNT(*) FROM task_executions WHERE taskId = :taskId AND isSuccess = 0")
    suspend fun getFailureCountByTaskId(taskId: String): Int
    
    @Query("SELECT AVG(executionTime) FROM task_executions WHERE taskId = :taskId")
    suspend fun getAverageExecutionTimeByTaskId(taskId: String): Float
    
    @Query("SELECT * FROM task_executions WHERE taskId = :taskId ORDER BY startTime DESC LIMIT 1")
    suspend fun getLastExecutionByTaskId(taskId: String): TaskExecutionEntity?
    
    @Query("SELECT AVG(cpuUsage) FROM task_executions WHERE startTime > :since")
    suspend fun getAverageCpuUsageSince(since: Long): Float
    
    @Query("SELECT AVG(memoryUsage) FROM task_executions WHERE startTime > :since")
    suspend fun getAverageMemoryUsageSince(since: Long): Float
    
    @Query("SELECT AVG(batteryUsage) FROM task_executions WHERE startTime > :since")
    suspend fun getAverageBatteryUsageSince(since: Long): Float
    
    @Query("DELETE FROM task_executions WHERE startTime < :before")
    suspend fun deleteExecutionsBefore(before: Long)
}