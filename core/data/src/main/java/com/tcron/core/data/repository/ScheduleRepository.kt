package com.tcron.core.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

data class Schedule(
    val id: String,
    val name: String,
    val description: String,
    val command: String,
    val cronExpression: String,
    val isEnabled: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val lastExecuted: Long? = null,
    val nextExecution: Long? = null
)

@Singleton
class ScheduleRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs: SharedPreferences = context.getSharedPreferences("tcron_schedules", Context.MODE_PRIVATE)
    private val gson = Gson()
    
    private val _schedules = MutableStateFlow(getSchedules())
    val schedules: StateFlow<List<Schedule>> = _schedules.asStateFlow()
    
    fun saveSchedule(schedule: Schedule) {
        try {
            val currentSchedules = getSchedules().toMutableList()
            val existingIndex = currentSchedules.indexOfFirst { it.id == schedule.id }
            
            if (existingIndex >= 0) {
                currentSchedules[existingIndex] = schedule
            } else {
                currentSchedules.add(schedule)
            }
            
            val json = gson.toJson(currentSchedules)
            prefs.edit().putString("schedules", json).commit() // Use commit() for immediate persistence
            _schedules.value = currentSchedules
        } catch (e: Exception) {
            android.util.Log.e("ScheduleRepository", "Error saving schedule: ${e.message}", e)
            throw e // Re-throw to let the UI handle the error
        }
    }
    
    fun deleteSchedule(scheduleId: String) {
        val currentSchedules = getSchedules().toMutableList()
        currentSchedules.removeAll { it.id == scheduleId }
        
        val json = gson.toJson(currentSchedules)
        prefs.edit().putString("schedules", json).apply()
        _schedules.value = currentSchedules
    }
    
    fun getScheduleById(id: String): Schedule? {
        return getSchedules().find { it.id == id }
    }
    
    fun updateScheduleStatus(id: String, isEnabled: Boolean) {
        val schedule = getScheduleById(id) ?: return
        saveSchedule(schedule.copy(isEnabled = isEnabled))
    }
    
    private fun getSchedules(): List<Schedule> {
        val json = prefs.getString("schedules", null) ?: return emptyList()
        val type = object : TypeToken<List<Schedule>>() {}.type
        return try {
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}