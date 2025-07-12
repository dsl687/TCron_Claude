package com.tcron.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

data class TCronNotification(
    val id: String,
    val taskName: String,
    val message: String,
    val timestamp: Long,
    val isRead: Boolean,
    val type: NotificationType,
    val details: String? = null
)

enum class NotificationType {
    TASK_SUCCESS, TASK_FAILURE, SYSTEM_ALERT, TASK_SCHEDULED
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    onNavigateBack: () -> Unit = {}
) {
    val notifications = remember { mutableStateListOf<TCronNotification>() }
    
    // TODO: Load real notifications from database/repository
    
    val unreadNotifications = notifications.filter { !it.isRead }
    val readNotifications = notifications.filter { it.isRead }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Notificações",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                },
                actions = {
                    if (unreadNotifications.isNotEmpty()) {
                        TextButton(
                            onClick = {
                                // Mark all as read
                                notifications.replaceAll { it.copy(isRead = true) }
                            }
                        ) {
                            Text("Marcar todas como lidas")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Unread notifications section
            if (unreadNotifications.isNotEmpty()) {
                item {
                    SectionHeader(
                        title = "Não lidas",
                        count = unreadNotifications.size
                    )
                }
                
                items(unreadNotifications) { notification ->
                    NotificationItem(
                        notification = notification,
                        onMarkAsRead = {
                            val index = notifications.indexOf(notification)
                            if (index != -1) {
                                notifications[index] = notification.copy(isRead = true)
                            }
                        },
                        onDelete = {
                            notifications.remove(notification)
                        }
                    )
                }
                
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
            
            // Read notifications section
            if (readNotifications.isNotEmpty()) {
                item {
                    SectionHeader(
                        title = "Lidas",
                        count = readNotifications.size
                    )
                }
                
                items(readNotifications) { notification ->
                    NotificationItem(
                        notification = notification,
                        onMarkAsRead = { /* Already read */ },
                        onDelete = {
                            notifications.remove(notification)
                        }
                    )
                }
            }
            
            // Empty state
            if (notifications.isEmpty()) {
                item {
                    EmptyNotificationsState()
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    count: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Text(
                text = count.toString(),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NotificationItem(
    notification: TCronNotification,
    onMarkAsRead: () -> Unit,
    onDelete: () -> Unit
) {
    val timeAgo = remember(notification.timestamp) {
        getTimeAgo(notification.timestamp)
    }
    
    var showMenu by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if (!notification.isRead) {
                    onMarkAsRead()
                }
            },
        colors = CardDefaults.cardColors(
            containerColor = if (notification.isRead) {
                MaterialTheme.colorScheme.surface
            } else {
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            }
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Unread indicator
            if (!notification.isRead) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(
                            MaterialTheme.colorScheme.primary,
                            CircleShape
                        )
                )
                Spacer(modifier = Modifier.width(12.dp))
            } else {
                Spacer(modifier = Modifier.width(20.dp))
            }
            
            // Notification icon
            Icon(
                imageVector = getNotificationIcon(notification.type),
                contentDescription = null,
                tint = getNotificationColor(notification.type),
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Notification content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = notification.taskName,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Text(
                    text = notification.message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                if (notification.details != null) {
                    Text(
                        text = notification.details,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
                
                Text(
                    text = timeAgo,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            
            // More options menu
            Box {
                IconButton(onClick = { showMenu = true }) {
                    Icon(
                        Icons.Default.MoreVert,
                        contentDescription = "Mais opções",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    if (!notification.isRead) {
                        DropdownMenuItem(
                            text = { Text("Marcar como lida") },
                            onClick = {
                                onMarkAsRead()
                                showMenu = false
                            },
                            leadingIcon = {
                                Icon(Icons.Default.MarkEmailRead, contentDescription = null)
                            }
                        )
                    }
                    
                    DropdownMenuItem(
                        text = { Text("Excluir") },
                        onClick = {
                            onDelete()
                            showMenu = false
                        },
                        leadingIcon = {
                            Icon(Icons.Default.Delete, contentDescription = null)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyNotificationsState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Default.Notifications,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Nenhuma notificação",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Text(
            text = "Você será notificado sobre execuções de tarefas e alertas do sistema",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            modifier = Modifier.padding(top = 8.dp, start = 32.dp, end = 32.dp)
        )
    }
}

private fun getNotificationIcon(type: NotificationType): ImageVector {
    return when (type) {
        NotificationType.TASK_SUCCESS -> Icons.Default.CheckCircle
        NotificationType.TASK_FAILURE -> Icons.Default.Error
        NotificationType.SYSTEM_ALERT -> Icons.Default.Warning
        NotificationType.TASK_SCHEDULED -> Icons.Default.Schedule
    }
}

private fun getNotificationColor(type: NotificationType): Color {
    return when (type) {
        NotificationType.TASK_SUCCESS -> Color(0xFF4CAF50)
        NotificationType.TASK_FAILURE -> Color(0xFFE57373)
        NotificationType.SYSTEM_ALERT -> Color(0xFFFFB74D)
        NotificationType.TASK_SCHEDULED -> Color(0xFF64B5F6)
    }
}

private fun getTimeAgo(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    
    return when {
        diff < 60_000 -> "Agora"
        diff < 3600_000 -> "${diff / 60_000}m atrás"
        diff < 86400_000 -> "${diff / 3600_000}h atrás"
        diff < 604800_000 -> "${diff / 86400_000}d atrás"
        else -> SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(timestamp))
    }
}

private fun getSampleNotifications(): List<TCronNotification> {
    val now = System.currentTimeMillis()
    return listOf(
        TCronNotification(
            id = "1",
            taskName = "Backup Diário",
            message = "Tarefa executada com sucesso",
            timestamp = now - 300_000, // 5 minutes ago
            isRead = false,
            type = NotificationType.TASK_SUCCESS,
            details = "Arquivos copiados: 127 | Tempo: 2.3s"
        ),
        TCronNotification(
            id = "2",
            taskName = "Limpeza Cache",
            message = "Falha na execução",
            timestamp = now - 1800_000, // 30 minutes ago
            isRead = false,
            type = NotificationType.TASK_FAILURE,
            details = "Erro: Permissão negada para /system/cache"
        ),
        TCronNotification(
            id = "3",
            taskName = "Sistema",
            message = "Uso de RAM acima de 80%",
            timestamp = now - 3600_000, // 1 hour ago
            isRead = true,
            type = NotificationType.SYSTEM_ALERT,
            details = "RAM atual: 6.2GB / 8GB"
        ),
        TCronNotification(
            id = "4",
            taskName = "Monitor Python",
            message = "Nova tarefa agendada",
            timestamp = now - 7200_000, // 2 hours ago
            isRead = true,
            type = NotificationType.TASK_SCHEDULED,
            details = "Próxima execução: Hoje às 18:00"
        )
    )
}