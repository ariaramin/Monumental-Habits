package com.ariaramin.monumentalhabits.Hilt.Modules

import android.content.Context
import com.ariaramin.monumentalhabits.Calendar.SingleRowCalendarManager
import com.ariaramin.monumentalhabits.PreferencesManager.SharedPreferencesManager
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
    fun provideSharedPreferencesManager(@ApplicationContext context: Context): SharedPreferencesManager {
        return SharedPreferencesManager(context)
    }

    @Provides
    fun provideSingleRowCalendarManager(): SingleRowCalendarManager {
        return SingleRowCalendarManager()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase? {
        return AppDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideDatabaseDao(appDatabase: AppDatabase): DatabaseDao {
        return appDatabase.databaseDao
    }
}