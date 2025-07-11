package com.tcron.core.domain.repository

import com.tcron.core.common.Result
import com.tcron.core.domain.model.TerminalCommand
import com.tcron.core.domain.model.TerminalConfig
import com.tcron.core.domain.model.TerminalOutput
import com.tcron.core.domain.model.TerminalSession
import kotlinx.coroutines.flow.Flow

interface TerminalRepository {
    
    fun getActiveSessions(): Flow<List<TerminalSession>>
    
    fun getSessionById(sessionId: String): Flow<TerminalSession?>
    
    suspend fun createSession(name: String, workingDirectory: String): Result<TerminalSession>
    
    suspend fun closeSession(sessionId: String): Result<Unit>
    
    suspend fun executeCommand(sessionId: String, command: String): Result<TerminalCommand>
    
    suspend fun executeCommandWithRoot(sessionId: String, command: String): Result<TerminalCommand>
    
    suspend fun getCommandHistory(sessionId: String): Result<List<TerminalCommand>>
    
    suspend fun getSessionOutput(sessionId: String): Result<List<TerminalOutput>>
    
    suspend fun clearSessionHistory(sessionId: String): Result<Unit>
    
    suspend fun clearSessionOutput(sessionId: String): Result<Unit>
    
    suspend fun saveSessionAsScript(sessionId: String, fileName: String): Result<String>
    
    suspend fun changeWorkingDirectory(sessionId: String, directory: String): Result<Unit>
    
    suspend fun getEnvironmentVariables(sessionId: String): Result<Map<String, String>>
    
    suspend fun setEnvironmentVariable(sessionId: String, key: String, value: String): Result<Unit>
    
    suspend fun getTerminalConfig(): Result<TerminalConfig>
    
    suspend fun updateTerminalConfig(config: TerminalConfig): Result<Unit>
    
    suspend fun isRootAvailable(): Result<Boolean>
    
    suspend fun requestRootPermission(): Result<Boolean>
    
    suspend fun killProcess(sessionId: String): Result<Unit>
    
    suspend fun getProcessList(): Result<List<String>>
    
    suspend fun exportSession(sessionId: String, format: String): Result<String>
}