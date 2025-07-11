package com.tcron.core.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tcron.core.data.database.entity.TaskEntity
import com.tcron.core.domain.model.TaskType
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    
    @Query("SELECT * FROM tasks ORDER BY updatedAt DESC")
    fun getAllTasks(): Flow<List<TaskEntity>>
    
    @Query("SELECT * FROM tasks WHERE id = :taskId")
    fun getTaskById(taskId: String): Flow<TaskEntity?>
    
    @Query("SELECT * FROM tasks WHERE type = :type ORDER BY updatedAt DESC")
    fun getTasksByType(type: TaskType): Flow<List<TaskEntity>>
    
    @Query("SELECT * FROM tasks WHERE isEnabled = 1 ORDER BY updatedAt DESC")
    fun getEnabledTasks(): Flow<List<TaskEntity>>
    
    @Query("SELECT * FROM tasks WHERE scheduledTime IS NOT NULL AND isEnabled = 1 ORDER BY scheduledTime ASC")
    fun getScheduledTasks(): Flow<List<TaskEntity>>
    
    @Query("SELECT * FROM tasks WHERE executeOnBoot = 1 AND isEnabled = 1")
    fun getBootTasks(): Flow<List<TaskEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity)
    
    @Update
    suspend fun updateTask(task: TaskEntity)
    
    @Delete
    suspend fun deleteTask(task: TaskEntity)
    
    @Query("DELETE FROM tasks WHERE id = :taskId")
    suspend fun deleteTaskById(taskId: String)
    
    @Query("UPDATE tasks SET isEnabled = :enabled, updatedAt = :updatedAt WHERE id = :taskId")
    suspend fun updateTaskEnabled(taskId: String, enabled: Boolean, updatedAt: Long)
    
    @Query("UPDATE tasks SET executionCount = :count, successCount = :successCount, failureCount = :failureCount, averageExecutionTime = :avgTime, lastExecutionTime = :lastTime, lastExecutionResult = :result, updatedAt = :updatedAt WHERE id = :taskId")
    suspend fun updateTaskExecution(
        taskId: String,
        count: Int,
        successCount: Int,
        failureCount: Int,
        avgTime: Long,
        lastTime: Long,
        result: String,
        updatedAt: Long
    )
    
    @Query("SELECT COUNT(*) FROM tasks")
    suspend fun getTaskCount(): Int
    
    @Query("SELECT COUNT(*) FROM tasks WHERE isEnabled = 1")
    suspend fun getEnabledTaskCount(): Int
    
    @Query("SELECT AVG(averageExecutionTime) FROM tasks WHERE executionCount > 0")
    suspend fun getAverageExecutionTime(): Float
    
    @Query("SELECT SUM(successCount) FROM tasks")
    suspend fun getTotalSuccessCount(): Int
    
    @Query("SELECT SUM(failureCount) FROM tasks")
    suspend fun getTotalFailureCount(): Int
    
    @Query("SELECT * FROM tasks WHERE name LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%'")
    fun searchTasks(query: String): Flow<List<TaskEntity>>
}