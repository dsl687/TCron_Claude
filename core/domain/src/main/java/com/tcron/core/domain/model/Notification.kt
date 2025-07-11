package com.tcron.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class AppNotification(
    val id: String,
    val title: String,
    val message: String,
    val type: NotificationType,
    val taskId: String?,
    val isRead: Boolean,
    val createdAt: Date,
    val readAt: Date?
) : Parcelable

@Parcelize
enum class NotificationType : Parcelable {
    TASK_STARTED,
    TASK_COMPLETED,
    TASK_FAILED,
    TASK_CANCELLED,
    SYSTEM_INFO,
    SYSTEM_WARNING,
    SYSTEM_ERROR
}

@Parcelize
data class NotificationSummary(
    val totalCount: Int,
    val unreadCount: Int,
    val taskCompletedCount: Int,
    val taskFailedCount: Int,
    val systemCount: Int
) : Parcelable