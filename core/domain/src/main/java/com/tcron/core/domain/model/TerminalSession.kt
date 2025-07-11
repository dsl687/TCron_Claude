package com.tcron.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class TerminalSession(
    val id: String,
    val name: String,
    val isActive: Boolean,
    val workingDirectory: String,
    val environment: Map<String, String>,
    val history: List<TerminalCommand>,
    val output: String,
    val createdAt: Date,
    val lastUsedAt: Date
) : Parcelable

@Parcelize
data class TerminalCommand(
    val id: String,
    val command: String,
    val workingDirectory: String,
    val exitCode: Int,
    val output: String,
    val errorOutput: String,
    val executionTime: Long,
    val timestamp: Date,
    val isSuccess: Boolean
) : Parcelable

@Parcelize
data class TerminalOutput(
    val content: String,
    val type: OutputType,
    val timestamp: Date,
    val isAnsiColored: Boolean
) : Parcelable

@Parcelize
enum class OutputType : Parcelable {
    STDOUT,
    STDERR,
    SYSTEM,
    COMMAND
}

@Parcelize
data class TerminalConfig(
    val fontSize: Float,
    val fontFamily: String,
    val backgroundColor: String,
    val foregroundColor: String,
    val cursorColor: String,
    val enableColors: Boolean,
    val enableBell: Boolean,
    val scrollbackLines: Int,
    val tabSize: Int
) : Parcelable