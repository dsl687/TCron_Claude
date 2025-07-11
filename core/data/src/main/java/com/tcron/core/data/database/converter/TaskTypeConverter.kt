package com.tcron.core.data.database.converter

import androidx.room.TypeConverter
import com.tcron.core.domain.model.TaskType

class TaskTypeConverter {
    @TypeConverter
    fun fromTaskType(taskType: TaskType): String {
        return taskType.name
    }

    @TypeConverter
    fun toTaskType(taskType: String): TaskType {
        return TaskType.valueOf(taskType)
    }
}