package com.tcron.core.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.tcron.core.data.database.converter.DateConverter
import com.tcron.core.data.database.converter.NotificationTypeConverter
import com.tcron.core.domain.model.NotificationType
import java.util.Date

@Entity(tableName = "notifications")
@TypeConverters(DateConverter::class, NotificationTypeConverter::class)
data class NotificationEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val message: String,
    val type: NotificationType,
    val taskId: String?,
    val isRead: Boolean,
    val createdAt: Date,
    val readAt: Date?
)