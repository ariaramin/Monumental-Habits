package com.ariaramin.monumentalhabits.Room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ariaramin.monumentalhabits.Models.Habit
import com.ariaramin.monumentalhabits.Utils.Constants

@Dao
interface DatabaseDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(habit: Habit)

    @Query("SELECT * FROM ${Constants.HABIT_TBL}")
    suspend fun read() : List<Habit>
}