package com.tcron.app.di

import com.tcron.core.domain.repository.DashboardRepository
import com.tcron.core.domain.repository.NotificationRepository
import com.tcron.core.domain.repository.SettingsRepository
import com.tcron.core.domain.repository.TaskRepository
import com.tcron.core.domain.repository.TerminalRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    
    // TODO: Implement repository providers once we create the actual implementations
    // For now, we'll create dummy implementations to make the app compile
    
    @Provides
    @Singleton
    fun provideTaskRepository(): TaskRepository {
        // TODO: Return actual implementation
        return DummyTaskRepository()
    }
    
    @Provides
    @Singleton
    fun provideTerminalRepository(): TerminalRepository {
        // TODO: Return actual implementation
        return DummyTerminalRepository()
    }
    
    @Provides
    @Singleton
    fun provideSettingsRepository(): SettingsRepository {
        // TODO: Return actual implementation
        return DummySettingsRepository()
    }
    
    @Provides
    @Singleton
    fun provideNotificationRepository(): NotificationRepository {
        // TODO: Return actual implementation
        return DummyNotificationRepository()
    }
    
    @Provides
    @Singleton
    fun provideDashboardRepository(): DashboardRepository {
        // TODO: Return actual implementation
        return DummyDashboardRepository()
    }
}