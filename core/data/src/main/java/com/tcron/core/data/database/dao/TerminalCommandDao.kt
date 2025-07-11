package com.tcron.core.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tcron.core.data.database.entity.TerminalCommandEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TerminalCommandDao {
    
    @Query("SELECT * FROM terminal_commands WHERE sessionId = :sessionId ORDER BY timestamp DESC")
    fun getCommandsBySessionId(sessionId: String): Flow<List<TerminalCommandEntity>>
    
    @Query("SELECT * FROM terminal_commands WHERE sessionId = :sessionId ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentCommandsBySessionId(sessionId: String, limit: Int): Flow<List<TerminalCommandEntity>>
    
    @Query("SELECT * FROM terminal_commands WHERE id = :commandId")
    suspend fun getCommandById(commandId: String): TerminalCommandEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCommand(command: TerminalCommandEntity)
    
    @Query("DELETE FROM terminal_commands WHERE sessionId = :sessionId")
    suspend fun deleteCommandsBySessionId(sessionId: String)
    
    @Query("DELETE FROM terminal_commands WHERE id = :commandId")
    suspend fun deleteCommandById(commandId: String)
    
    @Query("SELECT COUNT(*) FROM terminal_commands WHERE sessionId = :sessionId")
    suspend fun getCommandCountBySessionId(sessionId: String): Int
    
    @Query("SELECT COUNT(*) FROM terminal_commands WHERE sessionId = :sessionId AND isSuccess = 1")
    suspend fun getSuccessCountBySessionId(sessionId: String): Int
    
    @Query("SELECT COUNT(*) FROM terminal_commands WHERE sessionId = :sessionId AND isSuccess = 0")
    suspend fun getFailureCountBySessionId(sessionId: String): Int
    
    @Query("SELECT AVG(executionTime) FROM terminal_commands WHERE sessionId = :sessionId")
    suspend fun getAverageExecutionTimeBySessionId(sessionId: String): Float
    
    @Query("SELECT DISTINCT command FROM terminal_commands WHERE sessionId = :sessionId ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getUniqueCommandsBySessionId(sessionId: String, limit: Int): List<String>
    
    @Query("SELECT * FROM terminal_commands WHERE sessionId = :sessionId AND command LIKE '%' || :query || '%' ORDER BY timestamp DESC")
    fun searchCommandsBySessionId(sessionId: String, query: String): Flow<List<TerminalCommandEntity>>
    
    @Query("DELETE FROM terminal_commands WHERE timestamp < :before")
    suspend fun deleteCommandsBefore(before: Long)
    
    @Query("SELECT * FROM terminal_commands WHERE sessionId = :sessionId ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLastCommandBySessionId(sessionId: String): TerminalCommandEntity?
}