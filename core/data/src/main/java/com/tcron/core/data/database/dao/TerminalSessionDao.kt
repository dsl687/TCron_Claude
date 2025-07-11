package com.tcron.core.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tcron.core.data.database.entity.TerminalSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TerminalSessionDao {
    
    @Query("SELECT * FROM terminal_sessions ORDER BY lastUsedAt DESC")
    fun getAllSessions(): Flow<List<TerminalSessionEntity>>
    
    @Query("SELECT * FROM terminal_sessions WHERE isActive = 1 ORDER BY lastUsedAt DESC")
    fun getActiveSessions(): Flow<List<TerminalSessionEntity>>
    
    @Query("SELECT * FROM terminal_sessions WHERE id = :sessionId")
    fun getSessionById(sessionId: String): Flow<TerminalSessionEntity?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: TerminalSessionEntity)
    
    @Update
    suspend fun updateSession(session: TerminalSessionEntity)
    
    @Delete
    suspend fun deleteSession(session: TerminalSessionEntity)
    
    @Query("DELETE FROM terminal_sessions WHERE id = :sessionId")
    suspend fun deleteSessionById(sessionId: String)
    
    @Query("UPDATE terminal_sessions SET isActive = :active, lastUsedAt = :lastUsed WHERE id = :sessionId")
    suspend fun updateSessionActive(sessionId: String, active: Boolean, lastUsed: Long)
    
    @Query("UPDATE terminal_sessions SET output = :output, lastUsedAt = :lastUsed WHERE id = :sessionId")
    suspend fun updateSessionOutput(sessionId: String, output: String, lastUsed: Long)
    
    @Query("UPDATE terminal_sessions SET workingDirectory = :directory, lastUsedAt = :lastUsed WHERE id = :sessionId")
    suspend fun updateSessionWorkingDirectory(sessionId: String, directory: String, lastUsed: Long)
    
    @Query("UPDATE terminal_sessions SET environment = :environment, lastUsedAt = :lastUsed WHERE id = :sessionId")
    suspend fun updateSessionEnvironment(sessionId: String, environment: String, lastUsed: Long)
    
    @Query("SELECT COUNT(*) FROM terminal_sessions WHERE isActive = 1")
    suspend fun getActiveSessionCount(): Int
    
    @Query("UPDATE terminal_sessions SET isActive = 0 WHERE id != :exceptSessionId")
    suspend fun deactivateAllSessionsExcept(exceptSessionId: String)
    
    @Query("UPDATE terminal_sessions SET isActive = 0")
    suspend fun deactivateAllSessions()
}