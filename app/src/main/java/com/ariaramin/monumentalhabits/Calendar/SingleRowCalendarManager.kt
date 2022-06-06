package com.ariaramin.monumentalhabits.Calendar

import android.widget.TextView
import com.ariaramin.monumentalhabits.R
import com.michalsvec.singlerowcalendar.calendar.CalendarChangesObserver
import com.michalsvec.singlerowcalendar.calendar.CalendarViewManager
import com.michalsvec.singlerowcalendar.calendar.SingleRowCalendar
import com.michalsvec.singlerowcalendar.calendar.SingleRowCalendarAdapter
import com.michalsvec.singlerowcalendar.selection.CalendarSelectionManager
import com.michalsvec.singlerowcalendar.utils.DateUtils
import java.util.*

class SingleRowCalendarManager {

    private fun getCalendarViewManager(): CalendarViewManager {
        return object : CalendarViewManager {
            override fun setCalendarViewResourceId(
                position: Int,
                date: Date,
                isSelected: Boolean
            ): Int {
                // return item layout files, which you have created
                return if (isSelected) {
                    R.layout.single_row_calendar_item_selected
                } else {
                    R.layout.single_row_calendar_item_deselected
                }
            }

            override fun bindDataToCalendarView(
                holder: SingleRowCalendarAdapter.CalendarViewHolder,
                date: Date,
                position: Int,
                isSelected: Boolean
            ) {
                // bind data to calendar item views
                val dayTextView = holder.itemView.findViewById<TextView>(R.id.dayTextView)
                val dateTextView = holder.itemView.findViewById<TextView>(R.id.dateTextView)
                dayTextView.text = DateUtils.getDay3LettersName(date)
                dateTextView.text = DateUtils.getDayNumber(date)
            }
        }
    }

    private fun getSelectionManager(): CalendarSelectionManager {
        return object : CalendarSelectionManager {
            override fun canBeItemSelected(position: Int, date: Date): Boolean {
                return true
            }
        }
    }

//    private fun getCalendarChangesObserver(): CalendarChangesObserver {
//        return object : CalendarChangesObserver {
//            override fun whenWeekMonthYearChanged(
//                weekNumber: String,
//                monthNumber: String,
//                monthName: String,
//                year: String,
//                date: Date
//            ) {
//                super.whenWeekMonthYearChanged(weekNumber, monthNumber, monthName, year, date)
//            }
//
//            override fun whenSelectionChanged(isSelected: Boolean, position: Int, date: Date) {
//                super.whenSelectionChanged(isSelected, position, date)
//            }
//
//            override fun whenCalendarScrolled(dx: Int, dy: Int) {
//                super.whenCalendarScrolled(dx, dy)
//            }
//
//            override fun whenSelectionRestored() {
//                super.whenSelectionRestored()
//            }
//
//            override fun whenSelectionRefreshed() {
//                super.whenSelectionRefreshed()
//            }
//        }
//    }

    fun getCalendar(calendar: SingleRowCalendar, calendarObserver: CalendarChangesObserver): SingleRowCalendar {
        return calendar.apply {
            calendarViewManager = getCalendarViewManager()
            calendarChangesObserver = calendarObserver
            calendarSelectionManager = getSelectionManager()
            futureDaysCount = 3
            pastDaysCount = 3
            includeCurrentDate = true
            init()
        }
    }
}