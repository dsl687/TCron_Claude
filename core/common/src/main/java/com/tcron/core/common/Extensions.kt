package com.tcron.core.common

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun Context.openFile(file: File) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(Uri.fromFile(file), "text/plain")
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    startActivity(intent)
}

fun String.toFile(): File = File(this)

fun File.ensureExists(): File {
    if (!exists()) {
        if (isDirectory) {
            mkdirs()
        } else {
            parentFile?.mkdirs()
            createNewFile()
        }
    }
    return this
}

fun Date.formatDateTime(): String {
    val format = SimpleDateFormat("HH:mm:ss – dd/MM/yy", Locale.getDefault())
    return format.format(this)
}

fun Date.formatTime(): String {
    val format = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    return format.format(this)
}

fun Date.formatDate(): String {
    val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return format.format(this)
}

fun Long.formatDuration(): String {
    val hours = this / 3600
    val minutes = (this % 3600) / 60
    val seconds = this % 60
    
    return when {
        hours > 0 -> "${hours}h ${minutes}m ${seconds}s"
        minutes > 0 -> "${minutes}m ${seconds}s"
        else -> "${seconds}s"
    }
}

fun String.isValidScriptName(): Boolean {
    return this.isNotBlank() && 
           this.matches(Regex("^[a-zA-Z0-9_-]+$")) &&
           this.length <= 50
}

fun String.sanitizeForFilename(): String {
    return this.replace(Regex("[^a-zA-Z0-9_-]"), "_")
}

@RequiresApi(Build.VERSION_CODES.O)
fun String.executeCommand(): Process {
    return ProcessBuilder()
        .command("sh", "-c", this)
        .redirectErrorStream(true)
        .start()
}

@Composable
fun LaunchedEffectOnce(key: Any? = Unit, block: suspend () -> Unit) {
    val context = LocalContext.current
    LaunchedEffect(key) {
        block()
    }
}

fun <T> List<T>.takeIfNotEmpty(): List<T>? = if (isEmpty()) null else this

fun String.truncate(maxLength: Int): String {
    return if (length <= maxLength) this else "${take(maxLength - 3)}..."
}

fun ByteArray.toHexString(): String {
    return joinToString("") { "%02x".format(it) }
}

fun String.fromHexString(): ByteArray {
    return chunked(2).map { it.toInt(16).toByte() }.toByteArray()
}