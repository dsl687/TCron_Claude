package com.tcron.core.data.database.converter

import androidx.room.TypeConverter
import com.tcron.core.domain.model.NotificationType

class NotificationTypeConverter {
    @TypeConverter
    fun fromNotificationType(notificationType: NotificationType): String {
        return notificationType.name
    }

    @TypeConverter
    fun toNotificationType(notificationType: String): NotificationType {
        return NotificationType.valueOf(notificationType)
    }
}