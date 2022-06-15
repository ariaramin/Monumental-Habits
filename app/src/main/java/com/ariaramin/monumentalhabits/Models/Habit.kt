package com.ariaramin.monumentalhabits.Models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ariaramin.monumentalhabits.Utils.Constants
import java.util.*
import kotlin.collections.ArrayList

@Entity(tableName = Constants.HABIT_TBL)
data class Habit(
    val title: String,
    val days: List<String>,
    val color: Int,
    val reminderTime: String,
    val isNotificationOn: Boolean
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    var markedDates: List<Date> = ArrayList()
}