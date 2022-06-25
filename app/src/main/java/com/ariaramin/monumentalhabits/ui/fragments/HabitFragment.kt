package com.ariaramin.monumentalhabits.ui.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.ariaramin.monumentalhabits.Calendar.DayViewContainer
import com.ariaramin.monumentalhabits.Calendar.MonthViewContainer
import com.ariaramin.monumentalhabits.MainActivity
import com.ariaramin.monumentalhabits.MainViewModel
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
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.math.max

@AndroidEntryPoint
class HabitFragment : Fragment() {

    private lateinit var binding: FragmentHabitBinding
    private val mainViewModel by viewModels<MainViewModel>()
    private lateinit var mainActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHabitBinding.inflate(inflater, container, false)
        mainActivity.fab.setOnClickListener { view ->
            Navigation.findNavController(view)
                .navigate(R.id.action_habitFragment_to_addHabitFragment)
        }
        binding.backstackButton.setOnClickListener {
            requireActivity().onBackPressed()
        }
        val args = arguments
        args?.let {
            val habit = it.getParcelable<Habit>(Constants.HABIT)!!
            binding.pageTitleTextView.text = habit.title
            setupHabitDetails(habit)
            setupCalendar(habit)
            binding.editButton.setOnClickListener { view ->
                val bundle = Bundle()
                bundle.putParcelable(Constants.HABIT, habit)
                Navigation.findNavController(view)
                    .navigate(R.id.action_habitFragment_to_addHabitFragment, bundle)
            }
            binding.markAsCompleteButton.setOnClickListener { view ->
                markHabitAsCompleted(habit, view)
            }
            binding.markAsMissedButton.setOnClickListener { view ->
                markHabitAsMissed(habit, view)
            }
        }

        return binding.root
    }

    private fun markHabitAsCompleted(habit: Habit, view: View) {
        if (habit.days.contains(getDayOfTheWeek())) {
            if (!habit.markedAsCompletedDates.contains(getToday())) {
                val dates = habit.markedAsCompletedDates as ArrayList<String>
                dates.add(getToday())
            }
            mainViewModel.updateHabit(habit)
            Navigation.findNavController(view).navigate(R.id.action_habitFragment_to_homeFragment)
        } else {
            Toast.makeText(context, getString(R.string.not_include_today), Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun getDayOfTheWeek(): String {
        val sdf = SimpleDateFormat("EEE")
        val d = Date()
        return sdf.format(d).uppercase(Locale.getDefault())
    }

    private fun markHabitAsMissed(habit: Habit, view: View) {
        if (habit.markedAsCompletedDates.contains(getToday())) {
            val dates = habit.markedAsCompletedDates as ArrayList<String>
            dates.remove(getToday())
            mainViewModel.updateHabit(habit)
            Navigation.findNavController(view).navigate(R.id.action_habitFragment_to_homeFragment)
        } else {
            Toast.makeText(context, getString(R.string.not_completed), Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupCalendarHeader() {
        binding.calendarView.monthHeaderBinder =
            object : MonthHeaderFooterBinder<MonthViewContainer> {
                override fun create(view: View): MonthViewContainer = MonthViewContainer(view)
                override fun bind(container: MonthViewContainer, month: CalendarMonth) {}
            }
    }

    private fun setupCalendar(habit: Habit) {
        setupCalendarDay(habit)
        setupCalendarHeader()
        setMonthTextView()
        nextButtonHandler()
        previousButtonHandler()
    }

    private fun setupCalendarDay(habit: Habit) {
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
                if (habit.markedAsCompletedDates.contains(convertToString(day.date))) {
                    binding.calendarDayLayout.setCardBackgroundColor(habit.color)
                    binding.calendarDayTextView.setTextColor(Color.WHITE)
                }
            }
        }
    }

    private fun convertToString(localDate: LocalDate): String {
        return DateTimeFormatter.ofPattern(Constants.DATE_FORMAT).format(localDate)
    }

    private fun getToday(): String {
        val dateFormat = SimpleDateFormat(Constants.DATE_FORMAT)
        return dateFormat.format(Date())
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
        binding.longestStreakTextView.text = "${getLongestStreak(habit)} Days"
        binding.currentStreakTextView.text = "${getCurrentStreak(habit)} Days"
        binding.completionRateTextView.text = "${getCompletionRate(habit)}%"
        binding.finishedCountTextView.text = habit.markedAsCompletedDates.size.toString()
    }

    private fun getCompletionRate(habit: Habit): Int {
        val totalDatesCount = getDifferenceDates(habit)
        val markedDatesCount = habit.markedAsCompletedDates.size
        return if (totalDatesCount == 0) {
            if (markedDatesCount != 0) markedDatesCount * 100 else 0
        } else (markedDatesCount / totalDatesCount) * 100
    }

    private fun getDifferenceDates(habit: Habit): Int {
        val createdAt = SimpleDateFormat(Constants.DATE_FORMAT).format(Date(habit.createdAt))
        val today = LocalDate.parse(getToday())
        var start = LocalDate.parse(createdAt)
        val dates = ArrayList<LocalDate>()
        while (!start.isAfter(today)) {
            dates.add(start)
            start = start.plusDays(1)
        }
        val selectedDays = dates.filter { date ->
            val dayOfTheWeek = date.dayOfWeek.toString().subSequence(0, 3)
            habit.days.contains(dayOfTheWeek.toString().uppercase())
        }
        return selectedDays.size
    }

    private fun getCurrentStreak(habit: Habit): Int {
        var count = 0
        var currentStreak = 0
        val dates = habit.markedAsCompletedDates
        dates.map { date -> date.replace("-", "").toLong() }
        for (i in dates.indices) {
            if (i > 0) {
                val diff = getDifferenceDays(dates[i - 1].toLong(), dates[i].toLong())
                if (dates[i] == (dates[i - 1] + diff)) count++ else count = 0
            } else count = 0
            currentStreak = count
        }
        return currentStreak
    }

    private fun getLongestStreak(habit: Habit): Int {
        var count = 0
        var longestStreak = 0
        val dates = habit.markedAsCompletedDates
        dates.map { date -> date.replace("-", "").toLong() }
        for (i in dates.indices) {
            if (i > 0) {
                val diff = getDifferenceDays(dates[i - 1].toLong(), dates[i].toLong())
                if (dates[i] == (dates[i - 1] + diff)) count++ else count = 0
            } else count = 0
            longestStreak = max(longestStreak, count)
        }
        return longestStreak
    }

    private fun getDifferenceDays(date1: Long, date2: Long): Long {
        val diff = date2 - date1
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
    }

    private fun getRepeatDays(days: List<String>): String {
        return if (days.size == 7) {
            getString(R.string.repeat_everyday)
        } else {
            "${getString(R.string.repeat)} ${days.joinToString()}"
        }
    }
}