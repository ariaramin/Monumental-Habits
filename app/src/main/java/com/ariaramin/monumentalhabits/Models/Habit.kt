package com.ariaramin.monumentalhabits.Models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ariaramin.monumentalhabits.Utils.Constants

@Entity(tableName = Constants.HABIT_TBL)
data class Habit(
    val title: String,
    val days: List<String>,
    val reminderTime: String,
    val isNotificationOn: Boolean
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}