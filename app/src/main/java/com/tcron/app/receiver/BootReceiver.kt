package com.tcron.app.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.tcron.core.common.Logger
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {
    
    companion object {
        private const val TAG = "BootReceiver"
    }
    
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED ||
            intent.action == "android.intent.action.QUICKBOOT_POWERON") {
            
            Logger.i("Boot completed, starting scheduled tasks", TAG)
            
            // TODO: Start scheduled tasks that should run on boot
            // This will be implemented when we add the task scheduling functionality
        }
    }
}