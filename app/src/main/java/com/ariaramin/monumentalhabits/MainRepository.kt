package com.ariaramin.monumentalhabits

import com.ariaramin.monumentalhabits.Models.Habit
import com.ariaramin.monumentalhabits.Room.DatabaseDao

class MainRepository(private val databaseDao: DatabaseDao) {

    suspend fun insertHabit(habit: Habit) = databaseDao.insert(habit)

    suspend fun readHabits() = databaseDao.read()
}