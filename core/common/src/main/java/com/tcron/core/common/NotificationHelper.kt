package com.tcron.core.common

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val TAG = "NotificationHelper"
        const val CHANNEL_ID_TASK_EXECUTION = "task_execution"
        const val CHANNEL_ID_SYSTEM_ALERTS = "system_alerts"
        const val CHANNEL_ID_UPDATES = "updates"
        const val CHANNEL_ID_DEBUG = "debug"
        private const val TEST_NOTIFICATION_ID = 999
        private const val DEFAULT_NOTIFICATION_ID = 1001
    }
    
    init {
        createNotificationChannels()
    }
    
    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            
            // Task execution channel
            val taskChannel = NotificationChannel(
                CHANNEL_ID_TASK_EXECUTION,
                "Execução de Tarefas",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notificações sobre execução de tarefas agendadas"
            }
            
            // System alerts channel
            val systemChannel = NotificationChannel(
                CHANNEL_ID_SYSTEM_ALERTS,
                "Alertas do Sistema",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Alertas sobre problemas de sistema e recursos"
            }
            
            // Updates channel
            val updatesChannel = NotificationChannel(
                CHANNEL_ID_UPDATES,
                "Atualizações",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Notificações sobre atualizações do aplicativo"
            }
            
            // Debug channel
            val debugChannel = NotificationChannel(
                CHANNEL_ID_DEBUG,
                "Depuração",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Notificações de teste e depuração"
            }
            
            notificationManager.createNotificationChannels(listOf(
                taskChannel, systemChannel, updatesChannel, debugChannel
            ))
        }
    }
    
    fun sendTestNotification() {
        if (!hasNotificationPermission()) {
            return
        }
        
        try {
            Log.d(TAG, "Sending test notification")
            
            // Create intent to open the app (using package name instead of MainActivity class)
            val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)?.apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            } ?: Intent().apply {
                action = Intent.ACTION_MAIN
                addCategory(Intent.CATEGORY_LAUNCHER)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            
            val pendingIntent = PendingIntent.getActivity(
                context, 
                0, 
                intent, 
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            
            val notification = NotificationCompat.Builder(context, CHANNEL_ID_DEBUG)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("TCron - Teste de Notificação")
                .setContentText("Esta é uma notificação de teste. O sistema está funcionando corretamente!")
                .setStyle(NotificationCompat.BigTextStyle().bigText(
                    "Esta é uma notificação de teste do TCron. " +
                    "Se você está vendo isso, significa que as notificações estão funcionando corretamente. " +
                    "Você pode personalizar as configurações de notificação na tela de configurações."
                ))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setCategory(NotificationCompat.CATEGORY_STATUS)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .build()
            
            NotificationManagerCompat.from(context).notify(TEST_NOTIFICATION_ID, notification)
            Log.d(TAG, "Test notification sent successfully")
            
        } catch (e: Exception) {
            Log.e(TAG, "Error sending test notification", e)
        }
    }
    
    fun sendTaskCompletionNotification(taskName: String, output: String? = null) {
        if (!hasNotificationPermission()) return
        
        val notification = NotificationCompat.Builder(context, CHANNEL_ID_TASK_EXECUTION)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Tarefa Concluída")
            .setContentText("$taskName executada com sucesso")
            .apply {
                if (!output.isNullOrBlank()) {
                    setStyle(NotificationCompat.BigTextStyle().bigText("$taskName executada com sucesso\n\nSaída:\n$output"))
                }
            }
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()
        
        NotificationManagerCompat.from(context).notify(taskName.hashCode(), notification)
    }
    
    fun sendTaskFailureNotification(taskName: String, error: String) {
        if (!hasNotificationPermission()) return
        
        val notification = NotificationCompat.Builder(context, CHANNEL_ID_TASK_EXECUTION)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle("Falha na Tarefa")
            .setContentText("$taskName falhou na execução")
            .setStyle(NotificationCompat.BigTextStyle().bigText("$taskName falhou na execução\n\nErro:\n$error"))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()
        
        NotificationManagerCompat.from(context).notify(taskName.hashCode(), notification)
    }
    
    fun sendSystemAlert(title: String, message: String) {
        if (!hasNotificationPermission()) return
        
        val notification = NotificationCompat.Builder(context, CHANNEL_ID_SYSTEM_ALERTS)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()
        
        NotificationManagerCompat.from(context).notify(title.hashCode(), notification)
    }
    
    private fun hasNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            NotificationManagerCompat.from(context).areNotificationsEnabled()
        }
    }
    
    /**
     * Request notification permission using Android standard dialog
     */
    fun requestNotificationPermission(
        activity: ComponentActivity,
        onGranted: () -> Unit,
        onDenied: () -> Unit
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val requestPermissionLauncher = activity.registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted ->
                if (isGranted) {
                    Log.d(TAG, "Notification permission granted")
                    onGranted()
                } else {
                    Log.d(TAG, "Notification permission denied")
                    onDenied()
                }
            }
            
            when {
                hasNotificationPermission() -> {
                    Log.d(TAG, "Notification permission already granted")
                    onGranted()
                }
                ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    Manifest.permission.POST_NOTIFICATIONS
                ) -> {
                    Log.d(TAG, "Showing permission rationale")
                    // Show rationale and then request permission
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
                else -> {
                    Log.d(TAG, "Requesting notification permission")
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        } else {
            // For older Android versions, check if notifications are enabled
            if (NotificationManagerCompat.from(context).areNotificationsEnabled()) {
                Log.d(TAG, "Notifications are enabled on older Android version")
                onGranted()
            } else {
                Log.d(TAG, "Notifications are disabled on older Android version")
                onDenied()
            }
        }
    }
    
    /**
     * Check if we need to request notification permission
     */
    fun needsNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            !hasNotificationPermission()
        } else {
            !NotificationManagerCompat.from(context).areNotificationsEnabled()
        }
    }
    
    /**
     * Get notification permission status as text
     */
    fun getNotificationPermissionStatus(): String {
        return when {
            hasNotificationPermission() -> "Concedida"
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> "Negada"
            else -> "Desabilitada"
        }
    }
}