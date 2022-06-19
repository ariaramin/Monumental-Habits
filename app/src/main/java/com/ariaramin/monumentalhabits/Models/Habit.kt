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
    val title: String,
    val days: List<String>,
    val color: Int,
    val reminderTime: String,
    val isNotificationOn: Boolean
) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    var markedDates: List<Date> = ArrayList()

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