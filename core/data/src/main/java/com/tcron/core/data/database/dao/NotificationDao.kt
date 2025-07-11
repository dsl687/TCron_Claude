package com.tcron.core.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tcron.core.data.database.entity.NotificationEntity
import com.tcron.core.domain.model.NotificationType
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {
    
    @Query("SELECT * FROM notifications ORDER BY createdAt DESC")
    fun getAllNotifications(): Flow<List<NotificationEntity>>
    
    @Query("SELECT * FROM notifications WHERE isRead = 0 ORDER BY createdAt DESC")
    fun getUnreadNotifications(): Flow<List<NotificationEntity>>
    
    @Query("SELECT * FROM notifications WHERE id = :notificationId")
    suspend fun getNotificationById(notificationId: String): NotificationEntity?
    
    @Query("SELECT * FROM notifications WHERE type = :type ORDER BY createdAt DESC")
    fun getNotificationsByType(type: NotificationType): Flow<List<NotificationEntity>>
    
    @Query("SELECT * FROM notifications WHERE taskId = :taskId ORDER BY createdAt DESC")
    fun getNotificationsByTaskId(taskId: String): Flow<List<NotificationEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity)
    
    @Update
    suspend fun updateNotification(notification: NotificationEntity)
    
    @Delete
    suspend fun deleteNotification(notification: NotificationEntity)
    
    @Query("DELETE FROM notifications WHERE id = :notificationId")
    suspend fun deleteNotificationById(notificationId: String)
    
    @Query("DELETE FROM notifications")
    suspend fun deleteAllNotifications()
    
    @Query("DELETE FROM notifications WHERE isRead = 1")
    suspend fun deleteReadNotifications()
    
    @Query("UPDATE notifications SET isRead = 1, readAt = :readAt WHERE id = :notificationId")
    suspend fun markAsRead(notificationId: String, readAt: Long)
    
    @Query("UPDATE notifications SET isRead = 1, readAt = :readAt WHERE isRead = 0")
    suspend fun markAllAsRead(readAt: Long)
    
    @Query("SELECT COUNT(*) FROM notifications")
    suspend fun getNotificationCount(): Int
    
    @Query("SELECT COUNT(*) FROM notifications WHERE isRead = 0")
    suspend fun getUnreadNotificationCount(): Int
    
    @Query("SELECT COUNT(*) FROM notifications WHERE type = :type")
    suspend fun getNotificationCountByType(type: NotificationType): Int
    
    @Query("SELECT COUNT(*) FROM notifications WHERE taskId = :taskId")
    suspend fun getNotificationCountByTaskId(taskId: String): Int
    
    @Query("DELETE FROM notifications WHERE createdAt < :before")
    suspend fun deleteNotificationsBefore(before: Long)
    
    @Query("SELECT * FROM notifications WHERE title LIKE '%' || :query || '%' OR message LIKE '%' || :query || '%' ORDER BY createdAt DESC")
    fun searchNotifications(query: String): Flow<List<NotificationEntity>>
}