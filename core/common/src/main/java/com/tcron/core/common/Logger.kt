package com.tcron.core.common

import android.util.Log
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Logger {
    private const val TAG = "TCron"
    private var logFile: File? = null
    private var isFileLoggingEnabled = true
    
    fun init(logDirectory: File) {
        try {
            logDirectory.mkdirs()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val todayDate = dateFormat.format(Date())
            logFile = File(logDirectory, "tcron_$todayDate.log")
            
            if (!logFile!!.exists()) {
                logFile!!.createNewFile()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to initialize log file", e)
            isFileLoggingEnabled = false
        }
    }
    
    fun d(message: String, tag: String = TAG) {
        Log.d(tag, message)
        writeToFile("DEBUG", tag, message)
    }
    
    fun i(message: String, tag: String = TAG) {
        Log.i(tag, message)
        writeToFile("INFO", tag, message)
    }
    
    fun w(message: String, tag: String = TAG) {
        Log.w(tag, message)
        writeToFile("WARN", tag, message)
    }
    
    fun e(message: String, throwable: Throwable? = null, tag: String = TAG) {
        Log.e(tag, message, throwable)
        val fullMessage = if (throwable != null) {
            "$message\n${throwable.stackTraceToString()}"
        } else {
            message
        }
        writeToFile("ERROR", tag, fullMessage)
    }
    
    fun v(message: String, tag: String = TAG) {
        Log.v(tag, message)
        writeToFile("VERBOSE", tag, message)
    }
    
    private fun writeToFile(level: String, tag: String, message: String) {
        if (!isFileLoggingEnabled || logFile == null) return
        
        try {
            val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault()).format(Date())
            val logEntry = "$timestamp $level/$tag: $message\n"
            
            FileWriter(logFile, true).use { writer ->
                writer.write(logEntry)
            }
        } catch (e: IOException) {
            Log.e(TAG, "Failed to write to log file", e)
        }
    }
    
    fun getLogFile(): File? = logFile
    
    fun clearLogs() {
        logFile?.let { file ->
            if (file.exists()) {
                file.writeText("")
            }
        }
    }
}