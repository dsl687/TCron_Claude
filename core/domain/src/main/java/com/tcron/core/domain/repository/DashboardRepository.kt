package com.tcron.core.domain.repository

import com.tcron.core.common.Result
import com.tcron.core.domain.model.ChartData
import com.tcron.core.domain.model.DashboardMetrics
import com.tcron.core.domain.model.PerformanceData
import com.tcron.core.domain.model.SystemMetrics
import com.tcron.core.domain.model.TaskMetrics
import kotlinx.coroutines.flow.Flow

interface DashboardRepository {
    
    fun getDashboardMetrics(): Flow<DashboardMetrics>
    
    fun getSystemMetrics(): Flow<SystemMetrics>
    
    fun getTaskMetrics(): Flow<List<TaskMetrics>>
    
    fun getPerformanceData(): Flow<List<PerformanceData>>
    
    suspend fun refreshMetrics(): Result<Unit>
    
    suspend fun getTaskExecutionChart(): Result<ChartData>
    
    suspend fun getSuccessRateChart(): Result<ChartData>
    
    suspend fun getCpuUsageChart(): Result<ChartData>
    
    suspend fun getMemoryUsageChart(): Result<ChartData>
    
    suspend fun getBatteryUsageChart(): Result<ChartData>
    
    suspend fun getExecutionTimeChart(): Result<ChartData>
    
    suspend fun getTaskTypeDistribution(): Result<ChartData>
    
    suspend fun getFailureReasonChart(): Result<ChartData>
    
    suspend fun getCurrentSystemStatus(): Result<SystemMetrics>
    
    suspend fun getSystemMetricsHistory(hours: Int): Result<List<SystemMetrics>>
    
    suspend fun getTaskPerformanceHistory(taskId: String): Result<List<PerformanceData>>
    
    suspend fun exportDashboardData(format: String): Result<String>
    
    suspend fun clearMetricsHistory(): Result<Unit>
    
    suspend fun getResourceUsageByTask(): Result<Map<String, PerformanceData>>
    
    suspend fun getTaskExecutionTrends(): Result<Map<String, List<Float>>>
    
    suspend fun getSystemHealthScore(): Result<Float>
    
    suspend fun getPredictedBatteryDrain(): Result<Float>
    
    suspend fun getOptimizationSuggestions(): Result<List<String>>
}