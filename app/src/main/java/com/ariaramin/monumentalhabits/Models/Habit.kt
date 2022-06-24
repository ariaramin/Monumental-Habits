package com.ariaramin.monumentalhabits.Models

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ariaramin.monumentalhabits.Utils.Constants
import java.util.*
import kotlin.collections.ArrayList

@Entity(tableName = Constants.HABIT_TBL)
data class Habit(
    var title: String,
    var days: List<String>,
    var color: Int,
    var reminderTime: String,
    var isNotificationOn: Boolean
) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    var markedAsCompletedDates: List<String> = ArrayList()
    var createdAt: Long = Date().time

    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.createStringArrayList()!!,
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readByte() != 0.toByte()
    ) {
        id = parcel.readLong()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeStringList(days)
        parcel.writeInt(color)
        parcel.writeString(reminderTime)
        parcel.writeByte(if (isNotificationOn) 1 else 0)
        parcel.writeLong(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Habit> {
        override fun createFromParcel(parcel: Parcel): Habit {
            return Habit(parcel)
        }

        override fun newArray(size: Int): Array<Habit?> {
            return arrayOfNulls(size)
        }
    }
}