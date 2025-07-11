package com.tcron.core.data.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.tcron.core.data.database.converter.DateConverter
import java.util.Date

@Entity(
    tableName = "terminal_commands",
    foreignKeys = [
        ForeignKey(
            entity = TerminalSessionEntity::class,
            parentColumns = ["id"],
            childColumns = ["sessionId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
@TypeConverters(DateConverter::class)
data class TerminalCommandEntity(
    @PrimaryKey
    val id: String,
    val sessionId: String,
    val command: String,
    val workingDirectory: String,
    val exitCode: Int,
    val output: String,
    val errorOutput: String,
    val executionTime: Long,
    val timestamp: Date,
    val isSuccess: Boolean
)