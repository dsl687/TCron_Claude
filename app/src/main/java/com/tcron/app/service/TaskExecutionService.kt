package com.tcron.app.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.tcron.core.common.Logger
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskExecutionService : Service() {
    
    companion object {
        private const val TAG = "TaskExecutionService"
    }
    
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
    
    override fun onCreate() {
        super.onCreate()
        Logger.i("TaskExecutionService created", TAG)
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Logger.i("TaskExecutionService started", TAG)
        // TODO: Implement task execution logic
        return START_STICKY
    }
    
    override fun onDestroy() {
        super.onDestroy()
        Logger.i("TaskExecutionService destroyed", TAG)
    }
}