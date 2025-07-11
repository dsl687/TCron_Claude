package com.tcron.core.domain.repository

import com.tcron.core.common.Result
import com.tcron.core.domain.model.AppSettings
import com.tcron.core.domain.model.SecuritySettings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    
    fun getAppSettings(): Flow<AppSettings>
    
    suspend fun updateAppSettings(settings: AppSettings): Result<Unit>
    
    suspend fun resetToDefaults(): Result<Unit>
    
    suspend fun exportSettings(): Result<String>
    
    suspend fun importSettings(data: String): Result<Unit>
    
    suspend fun getSecuritySettings(): Result<SecuritySettings>
    
    suspend fun updateSecuritySettings(settings: SecuritySettings): Result<Unit>
    
    suspend fun isBiometricEnabled(): Result<Boolean>
    
    suspend fun setBiometricEnabled(enabled: Boolean): Result<Unit>
    
    suspend fun isEncryptionEnabled(): Result<Boolean>
    
    suspend fun setEncryptionEnabled(enabled: Boolean): Result<Unit>
    
    suspend fun getBaseDirectory(): Result<String>
    
    suspend fun setBaseDirectory(directory: String): Result<Unit>
    
    suspend fun validateDirectory(directory: String): Result<Boolean>
    
    suspend fun createDirectoryStructure(baseDirectory: String): Result<Unit>
    
    suspend fun getTheme(): Result<String>
    
    suspend fun setTheme(theme: String): Result<Unit>
    
    suspend fun getAccentColor(): Result<String>
    
    suspend fun setAccentColor(color: String): Result<Unit>
    
    suspend fun getLanguage(): Result<String>
    
    suspend fun setLanguage(language: String): Result<Unit>
    
    suspend fun isAutoStartEnabled(): Result<Boolean>
    
    suspend fun setAutoStartEnabled(enabled: Boolean): Result<Unit>
}