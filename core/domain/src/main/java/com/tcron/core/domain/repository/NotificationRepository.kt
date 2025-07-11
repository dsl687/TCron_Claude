package com.tcron.core.domain.repository

import com.tcron.core.common.Result
import com.tcron.core.domain.model.AppNotification
import com.tcron.core.domain.model.NotificationSummary
import com.tcron.core.domain.model.NotificationType
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    
    fun getAllNotifications(): Flow<List<AppNotification>>
    
    fun getUnreadNotifications(): Flow<List<AppNotification>>
    
    fun getNotificationSummary(): Flow<NotificationSummary>
    
    suspend fun createNotification(
        title: String,
        message: String,
        type: NotificationType,
        taskId: String? = null
    ): Result<AppNotification>
    
    suspend fun markAsRead(notificationId: String): Result<Unit>
    
    suspend fun markAllAsRead(): Result<Unit>
    
    suspend fun deleteNotification(notificationId: String): Result<Unit>
    
    suspend fun deleteAllNotifications(): Result<Unit>
    
    suspend fun deleteReadNotifications(): Result<Unit>
    
    suspend fun getNotificationById(notificationId: String): Result<AppNotification?>
    
    suspend fun getNotificationsByType(type: NotificationType): Result<List<AppNotification>>
    
    suspend fun getNotificationsByTask(taskId: String): Result<List<AppNotification>>
    
    suspend fun showSystemNotification(notification: AppNotification): Result<Unit>
    
    suspend fun cancelSystemNotification(notificationId: String): Result<Unit>
    
    suspend fun createNotificationChannel(): Result<Unit>
    
    suspend fun updateNotificationSettings(): Result<Unit>
}