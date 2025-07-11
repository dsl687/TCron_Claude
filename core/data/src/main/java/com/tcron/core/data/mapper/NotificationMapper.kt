package com.tcron.core.data.mapper

import com.tcron.core.data.database.entity.NotificationEntity
import com.tcron.core.domain.model.AppNotification

fun NotificationEntity.toDomainModel(): AppNotification {
    return AppNotification(
        id = id,
        title = title,
        message = message,
        type = type,
        taskId = taskId,
        isRead = isRead,
        createdAt = createdAt,
        readAt = readAt
    )
}

fun AppNotification.toEntity(): NotificationEntity {
    return NotificationEntity(
        id = id,
        title = title,
        message = message,
        type = type,
        taskId = taskId,
        isRead = isRead,
        createdAt = createdAt,
        readAt = readAt
    )
}