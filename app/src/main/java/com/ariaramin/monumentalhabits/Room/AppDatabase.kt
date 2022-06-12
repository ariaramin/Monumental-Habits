package com.ariaramin.monumentalhabits.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ariaramin.monumentalhabits.Models.Habit
import com.ariaramin.monumentalhabits.Utils.Constants

@Database(entities = [Habit::class], version = 1)
@TypeConverters(Converter::class)
abstract class AppDatabase: RoomDatabase() {

    abstract val databaseDao: DatabaseDao

    companion object {
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    Constants.APP_DATABASE)
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance!!
        }
    }

}