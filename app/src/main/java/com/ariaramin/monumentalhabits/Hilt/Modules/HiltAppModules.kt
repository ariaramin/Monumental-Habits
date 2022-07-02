package com.ariaramin.monumentalhabits.Hilt.Modules

import android.content.Context
import com.ariaramin.monumentalhabits.Calendar.SingleRowCalendarManager
import com.ariaramin.monumentalhabits.MainRepository
import com.ariaramin.monumentalhabits.Notification.NotificationServiceManager
import com.ariaramin.monumentalhabits.SharedPreferences.SharedPreferencesManager
import com.ariaramin.monumentalhabits.Room.AppDatabase
import com.ariaramin.monumentalhabits.Room.DatabaseDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class HiltAppModules {

    @Provides
    @Singleton
    fun provideSharedPreferencesManager(@ApplicationContext context: Context): SharedPreferencesManager {
        return SharedPreferencesManager(context)
    }

    @Provides
    @Singleton
    fun provideSingleRowCalendarManager(): SingleRowCalendarManager {
        return SingleRowCalendarManager()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideDatabaseDao(appDatabase: AppDatabase): DatabaseDao {
        return appDatabase.databaseDao
    }

    @Provides
    @Singleton
    fun provideMainRepository(databaseDao: DatabaseDao): MainRepository {
        return MainRepository(databaseDao)
    }

    @Provides
    @Singleton
    fun provideNotificationServiceManager(): NotificationServiceManager {
        return NotificationServiceManager()
    }
}