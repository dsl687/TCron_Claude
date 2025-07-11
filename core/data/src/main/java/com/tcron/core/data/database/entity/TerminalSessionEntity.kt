package com.tcron.core.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.tcron.core.data.database.converter.DateConverter
import java.util.Date

@Entity(tableName = "terminal_sessions")
@TypeConverters(DateConverter::class)
data class TerminalSessionEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val isActive: Boolean,
    val workingDirectory: String,
    val environment: String, // JSON string
    val output: String,
    val createdAt: Date,
    val lastUsedAt: Date
)