package com.ariaramin.monumentalhabits.Models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ariaramin.monumentalhabits.Utils.Constants

@Entity(tableName = Constants.HABIT_TBL)
data class Habit(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val title: String,
    val days: List<String>,
    val reminderTime: String,
    val isNotificationOn: Boolean
)