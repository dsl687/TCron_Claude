package com.tcron.core.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.tcron.core.data.database.converter.DateConverter
import com.tcron.core.data.database.converter.NotificationTypeConverter
import com.tcron.core.data.database.converter.TaskTypeConverter
import com.tcron.core.data.database.dao.NotificationDao
import com.tcron.core.data.database.dao.SystemMetricsDao
import com.tcron.core.data.database.dao.TaskDao
import com.tcron.core.data.database.dao.TaskExecutionDao
import com.tcron.core.data.database.dao.TerminalCommandDao
import com.tcron.core.data.database.dao.TerminalSessionDao
import com.tcron.core.data.database.entity.NotificationEntity
import com.tcron.core.data.database.entity.SystemMetricsEntity
import com.tcron.core.data.database.entity.TaskEntity
import com.tcron.core.data.database.entity.TaskExecutionEntity
import com.tcron.core.data.database.entity.TerminalCommandEntity
import com.tcron.core.data.database.entity.TerminalSessionEntity

@Database(
    entities = [
        TaskEntity::class,
        TaskExecutionEntity::class,
        TerminalSessionEntity::class,
        TerminalCommandEntity::class,
        NotificationEntity::class,
        SystemMetricsEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    DateConverter::class,
    TaskTypeConverter::class,
    NotificationTypeConverter::class
)
abstract class TCronDatabase : RoomDatabase() {
    
    abstract fun taskDao(): TaskDao
    abstract fun taskExecutionDao(): TaskExecutionDao
    abstract fun terminalSessionDao(): TerminalSessionDao
    abstract fun terminalCommandDao(): TerminalCommandDao
    abstract fun notificationDao(): NotificationDao
    abstract fun systemMetricsDao(): SystemMetricsDao
    
    companion object {
        @Volatile
        private var INSTANCE: TCronDatabase? = null
        
        fun getDatabase(context: Context): TCronDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TCronDatabase::class.java,
                    "tcron_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}