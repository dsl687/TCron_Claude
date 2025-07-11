package com.tcron.app.di

import android.content.Context
import androidx.room.Room
import com.tcron.core.data.database.TCronDatabase
import com.tcron.core.data.database.dao.NotificationDao
import com.tcron.core.data.database.dao.SystemMetricsDao
import com.tcron.core.data.database.dao.TaskDao
import com.tcron.core.data.database.dao.TaskExecutionDao
import com.tcron.core.data.database.dao.TerminalCommandDao
import com.tcron.core.data.database.dao.TerminalSessionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TCronDatabase {
        return Room.databaseBuilder(
            context,
            TCronDatabase::class.java,
            "tcron_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
    
    @Provides
    fun provideTaskDao(database: TCronDatabase): TaskDao {
        return database.taskDao()
    }
    
    @Provides
    fun provideTaskExecutionDao(database: TCronDatabase): TaskExecutionDao {
        return database.taskExecutionDao()
    }
    
    @Provides
    fun provideTerminalSessionDao(database: TCronDatabase): TerminalSessionDao {
        return database.terminalSessionDao()
    }
    
    @Provides
    fun provideTerminalCommandDao(database: TCronDatabase): TerminalCommandDao {
        return database.terminalCommandDao()
    }
    
    @Provides
    fun provideNotificationDao(database: TCronDatabase): NotificationDao {
        return database.notificationDao()
    }
    
    @Provides
    fun provideSystemMetricsDao(database: TCronDatabase): SystemMetricsDao {
        return database.systemMetricsDao()
    }
}