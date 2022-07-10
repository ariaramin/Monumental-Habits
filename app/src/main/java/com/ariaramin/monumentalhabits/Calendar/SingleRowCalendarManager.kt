package com.ariaramin.monumentalhabits.Calendar

import android.widget.TextView
import androidx.cardview.widget.CardView
import com.ariaramin.monumentalhabits.Models.Habit
import com.ariaramin.monumentalhabits.R
import com.ariaramin.monumentalhabits.Utils.Constants
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.michalsvec.singlerowcalendar.calendar.CalendarChangesObserver
import com.michalsvec.singlerowcalendar.calendar.CalendarViewManager
import com.michalsvec.singlerowcalendar.calendar.SingleRowCalendar
import com.michalsvec.singlerowcalendar.calendar.SingleRowCalendarAdapter
import com.michalsvec.singlerowcalendar.selection.CalendarSelectionManager
import com.michalsvec.singlerowcalendar.utils.DateUtils
import java.text.SimpleDateFormat
import java.util.*

class SingleRowCalendarManager {

    private fun getCalendarViewManager(
        tag: Int,
        habit: Habit?,
        habits: List<Habit>?
    ): CalendarViewManager {
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
                } else if (tag == Constants.TRACK_HABITS_TAG) {
                    R.layout.track_habit_item
                } else {
                    if (habit != null) {
                        if (habit.markedAsCompletedDates.contains(convertDateToString(date))) {
                            R.layout.habit_contribution_square_selected
                        } else {
                            R.layout.habit_contribution_square_deselected
                        }
                    } else {
                        R.layout.habit_contribution_square_deselected
                    }
                }
            }

            override fun bindDataToCalendarView(
                holder: SingleRowCalendarAdapter.CalendarViewHolder,
                date: Date,
                position: Int,
                isSelected: Boolean
            ) {
                // bind data to calendar item views
                when (tag) {
                    Constants.CALENDAR_TAG -> {
                        val dayTextView = holder.itemView.findViewById<TextView>(R.id.dayTextView)
                        val dateTextView = holder.itemView.findViewById<TextView>(R.id.dateTextView)
                        dayTextView.text = DateUtils.getDay3LettersName(date)
                        dateTextView.text = DateUtils.getDayNumber(date)
                    }
                    Constants.TRACK_HABITS_TAG -> {
                        val completedCountTextView =
                            holder.itemView.findViewById<TextView>(R.id.completedCountTextView)
                        val dateTextView = holder.itemView.findViewById<TextView>(R.id.dateTextView)
                        val progressBar =
                            holder.itemView.findViewById<CircularProgressIndicator>(R.id.progressBar)
                        if (android.text.format.DateUtils.isToday(date.time)) {
                            progressBar.trackColor =
                                progressBar.context.getColor(R.color.primaryColorLight)
                            progressBar.setIndicatorColor(progressBar.context.getColor(R.color.primaryColor))
                            dateTextView.text = dateTextView.context.getString(R.string.today)
                        } else {
                            progressBar.trackColor =
                                progressBar.context.getColor(R.color.onPrimaryLight)
                            progressBar.setIndicatorColor(progressBar.context.getColor(R.color.onPrimary))
                            dateTextView.text =
                                "${DateUtils.getMonthNumber(date)}/${DateUtils.getDayNumber(date)}"
                        }
                        completedCountTextView.text = habits?.let { getCompletedCount(it, date) }
                        progressBar.progress = habits?.let { getCompletionProgress(it, date) }!!
                    }
                    else -> {
                        habit?.let { habit ->
                            if (habit.markedAsCompletedDates.contains(convertDateToString(date))) {
                                val cardView =
                                    holder.itemView.findViewById<CardView>(R.id.habitContributionCardView)
                                cardView.setCardBackgroundColor(habit.color)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getCompletionProgress(habits: List<Habit>, today: Date): Int {
        var totalCount = 0
        var completedCount = 0
        for (habit in habits) {
            totalCount += habit.markedAsCompletedDates.size
            val completedToday =
                habit.markedAsCompletedDates.filter { item -> item == convertDateToString(today) }
            completedCount += completedToday.size
        }
        return if (totalCount == 0) {
            if (completedCount != 0) completedCount * 100 else 0
        } else ((completedCount.toDouble() / totalCount) * 100).toInt()
    }

    private fun getCompletedCount(habits: List<Habit>, today: Date): String {
        var completedCount = 0
        for (habit in habits) {
            val completedToday =
                habit.markedAsCompletedDates.filter { item -> item == convertDateToString(today) }
            completedCount += completedToday.size
        }
        return completedCount.toString()
    }

    private fun convertDateToString(date: Date): String {
        val dateFormat = SimpleDateFormat(Constants.DATE_FORMAT)
        return dateFormat.format(date)
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
        habits: List<Habit>?,
        calendarObserver: CalendarChangesObserver
    ): SingleRowCalendar {
        return calendar.apply {
            calendarViewManager = getCalendarViewManager(tag, habit, habits)
            calendarChangesObserver = calendarObserver
            calendarSelectionManager = getSelectionManager()
            pastDaysCount = 9
            futureDaysCount = 0
            initialPositionIndex = 9
            includeCurrentDate = true
            init()
        }
    }
}