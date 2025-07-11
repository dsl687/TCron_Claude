package com.tcron.core.common

object Constants {
    const val DATABASE_NAME = "tcron_database"
    const val PREFERENCES_NAME = "tcron_preferences"
    const val ENCRYPTED_PREFERENCES_NAME = "encrypted_tcron_preferences"
    
    // Terminal
    const val TERMINAL_HISTORY_SIZE = 1000
    const val TERMINAL_BUFFER_SIZE = 10000
    
    // Task execution
    const val TASK_TIMEOUT_DEFAULT = 30000L // 30 seconds
    const val TASK_TIMEOUT_LONG = 300000L // 5 minutes
    
    // Notifications
    const val NOTIFICATION_CHANNEL_ID = "tcron_notifications"
    const val NOTIFICATION_CHANNEL_NAME = "TCron Notifications"
    
    // File extensions
    const val SHELL_EXTENSION = ".sh"
    const val PYTHON_EXTENSION = ".py"
    const val JSON_EXTENSION = ".json"
    const val XML_EXTENSION = ".xml"
    const val LOG_EXTENSION = ".log"
    const val TXT_EXTENSION = ".txt"
    
    // Permissions
    const val PERMISSION_REQUEST_CODE = 1001
    
    // Work Manager
    const val WORK_TAG_TASK_EXECUTION = "task_execution"
    const val WORK_TAG_BOOT_TASKS = "boot_tasks"
    
    // Intent extras
    const val EXTRA_TASK_ID = "task_id"
    const val EXTRA_TASK_NAME = "task_name"
    const val EXTRA_SCRIPT_CONTENT = "script_content"
    
    // Default directories
    const val DEFAULT_SCRIPTS_DIR = "TCron/scripts"
    const val DEFAULT_LOGS_DIR = "TCron/logs"
    const val DEFAULT_BACKUPS_DIR = "TCron/backups"
}