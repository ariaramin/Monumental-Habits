package com.ariaramin.monumentalhabits.Room

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class Converter {

    @TypeConverter
    fun fromString(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun toString(value: List<String>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun fromDate(value: String): List<Date> {
        val listType = object : TypeToken<List<Long>>() {}.type
        val timestamps = Gson().fromJson(value, listType) as List<Long>
        return timestamps.map { item -> Date(item) }
    }

    @TypeConverter
    fun toDate(value: List<Date>): String {
        val timestamps = value.map { date -> date.time }
        return Gson().toJson(timestamps)
    }
}