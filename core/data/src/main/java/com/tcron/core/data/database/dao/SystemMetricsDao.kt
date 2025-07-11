package com.tcron.core.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tcron.core.data.database.entity.SystemMetricsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SystemMetricsDao {
    
    @Query("SELECT * FROM system_metrics ORDER BY timestamp DESC")
    fun getAllMetrics(): Flow<List<SystemMetricsEntity>>
    
    @Query("SELECT * FROM system_metrics ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentMetrics(limit: Int): Flow<List<SystemMetricsEntity>>
    
    @Query("SELECT * FROM system_metrics WHERE timestamp > :since ORDER BY timestamp DESC")
    fun getMetricsSince(since: Long): Flow<List<SystemMetricsEntity>>
    
    @Query("SELECT * FROM system_metrics WHERE timestamp BETWEEN :start AND :end ORDER BY timestamp DESC")
    fun getMetricsBetween(start: Long, end: Long): Flow<List<SystemMetricsEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMetrics(metrics: SystemMetricsEntity)
    
    @Query("DELETE FROM system_metrics WHERE timestamp < :before")
    suspend fun deleteMetricsBefore(before: Long)
    
    @Query("DELETE FROM system_metrics")
    suspend fun deleteAllMetrics()
    
    @Query("SELECT AVG(cpuUsage) FROM system_metrics WHERE timestamp > :since")
    suspend fun getAverageCpuUsageSince(since: Long): Float
    
    @Query("SELECT AVG(memoryUsage) FROM system_metrics WHERE timestamp > :since")
    suspend fun getAverageMemoryUsageSince(since: Long): Float
    
    @Query("SELECT AVG(batteryLevel) FROM system_metrics WHERE timestamp > :since")
    suspend fun getAverageBatteryLevelSince(since: Long): Float
    
    @Query("SELECT MAX(cpuUsage) FROM system_metrics WHERE timestamp > :since")
    suspend fun getMaxCpuUsageSince(since: Long): Float
    
    @Query("SELECT MAX(memoryUsage) FROM system_metrics WHERE timestamp > :since")
    suspend fun getMaxMemoryUsageSince(since: Long): Long
    
    @Query("SELECT MIN(batteryLevel) FROM system_metrics WHERE timestamp > :since")
    suspend fun getMinBatteryLevelSince(since: Long): Float
    
    @Query("SELECT COUNT(*) FROM system_metrics")
    suspend fun getMetricsCount(): Int
    
    @Query("SELECT * FROM system_metrics ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestMetrics(): SystemMetricsEntity?
}