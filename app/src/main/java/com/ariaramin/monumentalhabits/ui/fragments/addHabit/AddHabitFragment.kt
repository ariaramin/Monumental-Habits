package com.ariaramin.monumentalhabits.ui.fragments.addHabit

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ariaramin.monumentalhabits.MainViewModel
import com.ariaramin.monumentalhabits.Models.Habit
import com.ariaramin.monumentalhabits.R
import com.ariaramin.monumentalhabits.Utils.Constants
import com.ariaramin.monumentalhabits.databinding.FragmentAddHabitBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddHabitFragment : Fragment() {

    private lateinit var binding: FragmentAddHabitBinding
    private val mainViewModel by viewModels<MainViewModel>()
    private lateinit var fab: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddHabitBinding.inflate(inflater, container, false)
        fab = requireActivity().findViewById(R.id.fab)
        fab.setImageResource(R.drawable.ic_check)
        binding.backstackButton.setOnClickListener {
            requireActivity().onBackPressed()
        }
        setDaysClickListener()
        binding.reminderCardView.setOnClickListener {
            showReminderTimePicker()
        }
        fab.setOnClickListener {
            saveHabit()
        }

        return binding.root
    }

    private fun saveHabit() {
        val habit = createNewHabit()
        habit?.let {
            mainViewModel.insertHabit(it)
            findNavController().navigate(R.id.action_addHabitFragment_to_homeFragment)
        }
    }

    private fun createNewHabit(): Habit? {
        if (isDataValid()) {
            fab.isEnabled = true
            val title = binding.habitTitleTextView.text.toString()
            val selectedDays = getSelectedDays()
            val selectedDaysId = selectedDays.map { selectedDay -> selectedDay.id.toString() }
            val reminder = binding.reminderTextView.text.toString()
            val isNotificationOn = binding.notificationSwitch.isChecked
            return Habit(
                title,
                selectedDaysId,
                reminder,
                isNotificationOn
            )
        } else {
            fab.isEnabled = false
            return null
        }
    }

    private fun isDataValid(): Boolean {
        return isTitleEmpty() and getSelectedDays().isNotEmpty()
    }

    private fun getSelectedDays(): List<CardView> {
        val days = arrayOf(
            binding.sundayCardView,
            binding.mondayCardView,
            binding.tuesdayCardView,
            binding.wednesdayCardView,
            binding.thursdayCardView,
            binding.fridayCardView,
            binding.saturdayCardView
        )
        return days.filter { day ->
            day.tag.equals(Constants.SELECTED)
        }
    }

    private fun isTitleEmpty(): Boolean {
        val text = binding.habitTitleTextView.text.toString()
        return TextUtils.isEmpty(text)
    }

    private fun showReminderTimePicker() {
        val picker = MaterialTimePicker.Builder()
            .setTheme(R.style.TimePicker)
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setTitleText(getString(R.string.add_reminder))
            .build()
        picker.addOnPositiveButtonClickListener {
            val time = "${picker.hour}:${picker.minute} ${if (picker.hour < 12) "AM" else "PM"}"
            binding.reminderTextView.text = time
        }
        picker.show(parentFragmentManager, Constants.TIME_PICKER)
    }

    private fun setDaysClickListener() {
        val sunday = binding.sundayCardView
        val monday = binding.mondayCardView
        val tuesday = binding.tuesdayCardView
        val wednesday = binding.wednesdayCardView
        val thursday = binding.thursdayCardView
        val friday = binding.fridayCardView
        val saturday = binding.saturdayCardView
        val clickListener = View.OnClickListener { v ->
            when (v.id) {
                (sunday.id) -> selectDay(sunday)
                (monday.id) -> selectDay(monday)
                (tuesday.id) -> selectDay(tuesday)
                (wednesday.id) -> selectDay(wednesday)
                (thursday.id) -> selectDay(thursday)
                (friday.id) -> selectDay(friday)
                (saturday.id) -> selectDay(saturday)
            }
        }
        sunday.setOnClickListener(clickListener)
        monday.setOnClickListener(clickListener)
        tuesday.setOnClickListener(clickListener)
        wednesday.setOnClickListener(clickListener)
        thursday.setOnClickListener(clickListener)
        friday.setOnClickListener(clickListener)
        saturday.setOnClickListener(clickListener)
    }

    private fun selectDay(day: CardView) {
        val primaryColor = ContextCompat.getColor(
            requireContext(),
            R.color.primaryColor
        )
        if (day.cardBackgroundColor.defaultColor == primaryColor) {
            day.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.secondaryColorLight
                )
            )
            day.tag = Constants.UNSELECTED
        } else {
            day.setCardBackgroundColor(primaryColor)
            day.tag = Constants.SELECTED
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!fab.isEnabled) fab.isEnabled = true
        fab.setImageResource(R.drawable.ic_add)
    }
}