package com.ariaramin.monumentalhabits.Calendar

import android.widget.TextView
import com.ariaramin.monumentalhabits.Models.Habit
import com.ariaramin.monumentalhabits.R
import com.ariaramin.monumentalhabits.Utils.Constants
import com.michalsvec.singlerowcalendar.calendar.CalendarChangesObserver
import com.michalsvec.singlerowcalendar.calendar.CalendarViewManager
import com.michalsvec.singlerowcalendar.calendar.SingleRowCalendar
import com.michalsvec.singlerowcalendar.calendar.SingleRowCalendarAdapter
import com.michalsvec.singlerowcalendar.selection.CalendarSelectionManager
import com.michalsvec.singlerowcalendar.utils.DateUtils
import java.util.*

class SingleRowCalendarManager {

    private fun getCalendarViewManager(tag: Int, habit: Habit?): CalendarViewManager {
        return object : CalendarViewManager {
            override fun setCalendarViewResourceId(
                position: Int,
                date: Date,
                isSelected: Boolean
            ): Int {
                // return item layout files, which you have created
                return if (tag == Constants.CALENDAR_TAG) {
                    if (android.text.format.DateUtils.isToday(date.time)) {
                        R.layout.single_row_calendar_item_selected
                    } else {
                        R.layout.single_row_calendar_item_deselected
                    }
                } else {
                    habit?.let { habit ->
                        if (habit.markedDates.contains(date)) {
                            R.layout.habit_contribution_square_selected
                        } else {
                            R.layout.habit_contribution_square_deselected
                        }
                    }
                    R.layout.habit_contribution_square_deselected
                }
            }

            override fun bindDataToCalendarView(
                holder: SingleRowCalendarAdapter.CalendarViewHolder,
                date: Date,
                position: Int,
                isSelected: Boolean
            ) {
                // bind data to calendar item views
                if (tag == 0) {
                    val dayTextView = holder.itemView.findViewById<TextView>(R.id.dayTextView)
                    val dateTextView = holder.itemView.findViewById<TextView>(R.id.dateTextView)
                    dayTextView.text = DateUtils.getDay3LettersName(date)
                    dateTextView.text = DateUtils.getDayNumber(date)
                }
            }
        }
    }

    private fun getSelectionManager(): CalendarSelectionManager {
        return object : CalendarSelectionManager {
            override fun canBeItemSelected(position: Int, date: Date): Boolean {
                return false
            }
        }
    }

    fun getCalendar(
        calendar: SingleRowCalendar,
        tag: Int, habit: Habit?,
        calendarObserver: CalendarChangesObserver
    ): SingleRowCalendar {
        return calendar.apply {
            calendarViewManager = getCalendarViewManager(tag, habit)
            calendarChangesObserver = calendarObserver
            calendarSelectionManager = getSelectionManager()
            pastDaysCount = 6
            futureDaysCount = 0
            initialPositionIndex = 6
            includeCurrentDate = true
            init()
        }
    }
}