package com.tcron.app.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.tcron.core.common.Logger
import com.tcron.core.common.BusyBoxExecutor
import com.tcron.core.common.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TaskExecutionService : Service() {
    
    companion object {
        private const val TAG = "TaskExecutionService"
        const val ACTION_EXECUTE_SCRIPT = "execute_script"
        const val EXTRA_SCRIPT_PATH = "script_path"
        const val EXTRA_SCRIPT_TYPE = "script_type"
        const val EXTRA_USE_ROOT = "use_root"
        
        const val SCRIPT_TYPE_SHELL = "shell"
        const val SCRIPT_TYPE_PYTHON = "python"
    }
    
    @Inject
    lateinit var busyBoxExecutor: BusyBoxExecutor
    
    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
    
    override fun onCreate() {
        super.onCreate()
        Logger.i("TaskExecutionService created", TAG)
        
        // Initialize BusyBox on service creation
        serviceScope.launch {
            initializeBusyBox()
        }
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Logger.i("TaskExecutionService started", TAG)
        
        intent?.let { processIntent(it) }
        
        return START_STICKY
    }
    
    override fun onDestroy() {
        super.onDestroy()
        Logger.i("TaskExecutionService destroyed", TAG)
    }
    
    private fun processIntent(intent: Intent) {
        when (intent.action) {
            ACTION_EXECUTE_SCRIPT -> {
                val scriptPath = intent.getStringExtra(EXTRA_SCRIPT_PATH) ?: return
                val scriptType = intent.getStringExtra(EXTRA_SCRIPT_TYPE) ?: SCRIPT_TYPE_SHELL
                val useRoot = intent.getBooleanExtra(EXTRA_USE_ROOT, true)
                
                serviceScope.launch {
                    executeScript(scriptPath, scriptType, useRoot)
                }
            }
        }
    }
    
    private suspend fun initializeBusyBox() {
        Logger.i("Initializing BusyBox...", TAG)
        
        when (val result = busyBoxExecutor.initializeBusyBox()) {
            is Result.Success -> {
                Logger.i("BusyBox initialization successful: ${result.data}", TAG)
            }
            is Result.Error -> {
                Logger.e("BusyBox initialization failed: ${result.exception.message}", result.exception, TAG)
            }
            is Result.Loading -> {
                // Handle if needed
            }
        }
    }
    
    private suspend fun executeScript(scriptPath: String, scriptType: String, useRoot: Boolean) {
        Logger.i("Executing $scriptType script: $scriptPath (root: $useRoot)", TAG)
        
        val result = when (scriptType) {
            SCRIPT_TYPE_SHELL -> {
                busyBoxExecutor.executeShellScript(scriptPath, useRoot)
            }
            SCRIPT_TYPE_PYTHON -> {
                busyBoxExecutor.executePythonScript(scriptPath, useRoot = useRoot)
            }
            else -> {
                Logger.e("Unknown script type: $scriptType", tag = TAG)
                return
            }
        }
        
        when (result) {
            is Result.Success -> {
                val executionResult = result.data
                Logger.i("Script execution completed with exit code: ${executionResult.exitCode}", TAG)
                
                if (executionResult.output.isNotEmpty()) {
                    Logger.d("Script output: ${executionResult.output}", TAG)
                }
                
                if (executionResult.errorOutput.isNotEmpty()) {
                    Logger.w("Script error output: ${executionResult.errorOutput}", TAG)
                }
            }
            is Result.Error -> {
                Logger.e("Script execution failed: ${result.exception.message}", result.exception, TAG)
            }
            is Result.Loading -> {
                // Handle if needed
            }
        }
    }
}