package com.tcron.core.common

import android.app.ActivityManager
import android.content.Context
import android.os.BatteryManager
import android.os.Debug
import android.os.StatFs
import android.os.Environment
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

data class SystemMetrics(
    val cpuUsage: Float,
    val ramUsage: Long,
    val totalRam: Long,
    val batteryLevel: Int,
    val diskUsage: Long,
    val totalDisk: Long,
    val tasksExecuted: Int,
    val tasksFailed: Int,
    val averageExecutionTime: String
)

@Singleton
class SystemInfoManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    
    fun getCurrentMetrics(): SystemMetrics {
        return SystemMetrics(
            cpuUsage = getCpuUsage(),
            ramUsage = getRamUsage(),
            totalRam = getTotalRam(),
            batteryLevel = getBatteryLevel(),
            diskUsage = getDiskUsage(),
            totalDisk = getTotalDisk(),
            tasksExecuted = getTasksExecuted(),
            tasksFailed = getTasksFailed(),
            averageExecutionTime = getAverageExecutionTime()
        )
    }
    
    private fun getCpuUsage(): Float {
        return try {
            val memoryInfo = Debug.MemoryInfo()
            Debug.getMemoryInfo(memoryInfo)
            // Simplified CPU calculation - in real implementation would read /proc/stat
            (25..45).random().toFloat()
        } catch (e: Exception) {
            0f
        }
    }
    
    private fun getRamUsage(): Long {
        return try {
            val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val memoryInfo = ActivityManager.MemoryInfo()
            activityManager.getMemoryInfo(memoryInfo)
            (memoryInfo.totalMem - memoryInfo.availMem) / (1024 * 1024) // MB
        } catch (e: Exception) {
            0L
        }
    }
    
    private fun getTotalRam(): Long {
        return try {
            val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val memoryInfo = ActivityManager.MemoryInfo()
            activityManager.getMemoryInfo(memoryInfo)
            memoryInfo.totalMem / (1024 * 1024) // MB
        } catch (e: Exception) {
            0L
        }
    }
    
    private fun getBatteryLevel(): Int {
        return try {
            val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
            batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        } catch (e: Exception) {
            0
        }
    }
    
    private fun getDiskUsage(): Long {
        return try {
            val statFs = StatFs(Environment.getDataDirectory().path)
            val total = statFs.totalBytes
            val available = statFs.availableBytes
            (total - available) / (1024 * 1024) // MB
        } catch (e: Exception) {
            0L
        }
    }
    
    private fun getTotalDisk(): Long {
        return try {
            val statFs = StatFs(Environment.getDataDirectory().path)
            statFs.totalBytes / (1024 * 1024) // MB
        } catch (e: Exception) {
            0L
        }
    }
    
    private fun getTasksExecuted(): Int {
        // TODO: Implement actual task counting from database
        return (8..25).random()
    }
    
    private fun getTasksFailed(): Int {
        // TODO: Implement actual failed task counting from database
        return (0..5).random()
    }
    
    private fun getAverageExecutionTime(): String {
        // TODO: Implement actual average calculation from database
        val seconds = (5..45).random()
        return "${seconds}s"
    }
    
    fun isRootAvailable(): Boolean {
        return try {
            val process = Runtime.getRuntime().exec("su -c 'echo test'")
            process.waitFor() == 0
        } catch (e: Exception) {
            false
        }
    }
    
    fun getSystemUptime(): String {
        return try {
            val uptimeFile = File("/proc/uptime")
            if (uptimeFile.exists()) {
                val uptime = uptimeFile.readText().split(" ")[0].toDouble()
                val hours = (uptime / 3600).toInt()
                val minutes = ((uptime % 3600) / 60).toInt()
                "${hours}h ${minutes}m"
            } else {
                "N/A"
            }
        } catch (e: Exception) {
            "N/A"
        }
    }
}