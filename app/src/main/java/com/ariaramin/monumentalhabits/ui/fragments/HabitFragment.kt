package com.ariaramin.monumentalhabits.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.ariaramin.monumentalhabits.Calendar.DayViewContainer
import com.ariaramin.monumentalhabits.Calendar.MonthViewContainer
import com.ariaramin.monumentalhabits.Models.Habit
import com.ariaramin.monumentalhabits.R
import com.ariaramin.monumentalhabits.Utils.Constants
import com.ariaramin.monumentalhabits.databinding.FragmentHabitBinding
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.utils.next
import com.kizitonwose.calendarview.utils.previous
import java.sql.Date
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.*


class HabitFragment : Fragment() {

    private lateinit var binding: FragmentHabitBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHabitBinding.inflate(inflater, container, false)
        val args = arguments
        args?.let { bundle ->
            val habit = bundle.getParcelable<Habit>(Constants.HABIT)!!
            binding.pageTitleTextView.text = habit.title
            setupHabitDetails(habit)
            setupCalendar(habit)
        }
        setupCalendarHeader()
        setMonthTextView()
        nextButtonHandler()
        previousButtonHandler()

        return binding.root
    }


    private fun setupCalendarHeader() {
        binding.calendarView.monthHeaderBinder =
            object : MonthHeaderFooterBinder<MonthViewContainer> {
                override fun create(view: View): MonthViewContainer = MonthViewContainer(view)
                override fun bind(container: MonthViewContainer, month: CalendarMonth) {}
            }
    }

    private fun setupCalendar(habit: Habit) {
        setupCalendarDate()
        binding.calendarView.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)

            override fun bind(container: DayViewContainer, day: CalendarDay) {
                val binding = container.itemView
                binding.calendarDayTextView.text = day.date.dayOfMonth.toString()
                if (day.owner == DayOwner.THIS_MONTH) {
                    binding.calendarDayLayout.alpha = 1f
                } else {
                    binding.calendarDayLayout.alpha = 0.5f
                }
                if (habit.markedDates.contains(Date.valueOf(day.date.toString()))) {
                    binding.calendarDayLayout.setCardBackgroundColor(habit.color)
                    binding.calendarDayTextView.setTextColor(R.color.backgroundColor)
                }
            }
        }
    }

    private fun setMonthTextView() {
        binding.calendarView.monthScrollListener = { month ->
            val monthString = DateTimeFormatter.ofPattern("MMMM").format(month.yearMonth)
            val title = "$monthString ${month.yearMonth.year}"
            binding.monthYearTextView.text = title
        }
    }

    private fun nextButtonHandler() {
        binding.nextMonthImageView.setOnClickListener {
            binding.calendarView.findFirstVisibleMonth()?.let {
                binding.calendarView.smoothScrollToMonth(it.yearMonth.next)
            }
        }
    }

    private fun previousButtonHandler() {
        binding.previousMonthImageView.setOnClickListener {
            binding.calendarView.findFirstVisibleMonth()?.let {
                binding.calendarView.smoothScrollToMonth(it.yearMonth.previous)
            }
        }
    }

    private fun setupCalendarDate() {
        val currentMonth = YearMonth.now()
        val firstMonth = currentMonth.minusMonths(10)
        val lastMonth = currentMonth.plusMonths(10)
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
        binding.calendarView.setup(firstMonth, lastMonth, firstDayOfWeek)
        binding.calendarView.scrollToMonth(currentMonth)
    }

    private fun setupHabitDetails(habit: Habit) {
        binding.habitTitleTextView.text = habit.title
        binding.repeatDaysTextView.text = getRepeatDays(habit.days)
        binding.reminderTextView.text = habit.reminderTime
    }

    private fun getRepeatDays(days: List<String>): String {
        return if (days.size == 7) {
            getString(R.string.repeat_everyday)
        } else {
            "${getString(R.string.repeat)} ${days.joinToString()}"
        }
    }
}