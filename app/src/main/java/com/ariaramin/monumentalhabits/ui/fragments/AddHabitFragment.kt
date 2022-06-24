package com.ariaramin.monumentalhabits.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
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
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddHabitFragment : Fragment() {

    private lateinit var binding: FragmentAddHabitBinding
    private val mainViewModel by viewModels<MainViewModel>()
    private lateinit var fab: FloatingActionButton
    private var selectedColor: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddHabitBinding.inflate(inflater, container, false)
        fab = requireActivity().findViewById(R.id.fab)
        changeFabIconAnimation(R.drawable.ic_check)
        fab.isEnabled = isDataValid()
        selectedColor = binding.firstColorCardView.cardBackgroundColor.defaultColor
        val args = arguments
        if (args != null) {
            val habit = args.getParcelable<Habit>(Constants.HABIT)!!
            selectedColor = habit.color
            setupHabitDetail(habit)
            fab.isEnabled = isDataValid()
            binding.deleteButton.visibility = View.VISIBLE
            binding.deleteButton.setOnClickListener {
                deleteHabit(habit)
            }
            fab.setOnClickListener {
                updateHabit(habit)
            }
        } else {
            binding.deleteButton.visibility = View.GONE
            fab.setOnClickListener {
                saveHabit()
            }
        }
        checkEnteredTitle()
        setDaysClickListener()
        setColorClickListener()
        binding.backstackButton.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.reminderCardView.setOnClickListener {
            showReminderTimePicker()
        }

        return binding.root
    }

    private fun deleteHabit(habit: Habit) {
        mainViewModel.deleteHabit(habit)
        findNavController().navigate(R.id.action_addHabitFragment_to_homeFragment)
    }

    private fun updateHabit(habit: Habit) {
        updateHabitInfo(habit)
        mainViewModel.updateHabit(habit)
        findNavController().navigate(R.id.action_addHabitFragment_to_homeFragment)
    }

    private fun updateHabitInfo(habit: Habit) {
        if (isDataValid()) {
            val title = binding.habitTitleTextView.text.toString()
            val selectedDays = getSelectedDays()
            val selectedDaysId = selectedDays.map { selectedDay ->
                (selectedDay.parent as LinearLayout).tag.toString()
            }
            val reminder = binding.reminderTextView.text.toString()
            val isNotificationOn = binding.notificationSwitch.isChecked
            habit.title = title
            habit.days = selectedDaysId
            habit.color = selectedColor
            habit.reminderTime = reminder
            habit.isNotificationOn = isNotificationOn
        }
    }

    private fun setupHabitDetail(habit: Habit) {
        binding.habitTitleTextView.setText(habit.title)
        setupHabitDays(habit)
        setupHabitColor(habit)
        binding.reminderTextView.text = habit.reminderTime
        binding.notificationSwitch.isChecked = habit.isNotificationOn
    }

    private fun setupHabitDays(habit: Habit) {
        val dayCardViews = getDayCardViews()
        for (day in dayCardViews) {
            val tag = (day.parent as LinearLayout).tag.toString()
            if (habit.days.contains(tag)) {
                selectDay(day)
            }
        }
    }

    private fun setupHabitColor(habit: Habit) {
        val colorCardViews = getColorCardViews()
        for (color in colorCardViews) {
            if (color.cardBackgroundColor.defaultColor == habit.color) {
                selectColor(color, colorCardViews.filter { cardView -> cardView != color })
            }
        }
    }

    private fun getColorCardViews(): ArrayList<MaterialCardView> {
        return arrayListOf(
            binding.firstColorCardView,
            binding.secondColorCardView,
            binding.thirdColorCardView,
            binding.fourthColorCardView,
            binding.fifthColorCardView,
            binding.sixthColorCardView,
            binding.seventhColorCardView
        )
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
            val title = binding.habitTitleTextView.text.toString()
            val selectedDays = getSelectedDays()
            val selectedDaysId = selectedDays.map { selectedDay ->
                (selectedDay.parent as LinearLayout).tag.toString()
            }
            val reminder = binding.reminderTextView.text.toString()
            val isNotificationOn = binding.notificationSwitch.isChecked
            return Habit(
                title,
                selectedDaysId,
                selectedColor,
                reminder,
                isNotificationOn
            )
        }
        return null
    }

    private fun showReminderTimePicker() {
        val picker = MaterialTimePicker.Builder()
            .setTheme(R.style.TimePicker)
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setTitleText(getString(R.string.add_reminder))
            .build()
        picker.addOnPositiveButtonClickListener {
            setReminderTimeToTextView(picker)
        }
        picker.show(parentFragmentManager, Constants.TIME_PICKER)
    }

    private fun setReminderTimeToTextView(picker: MaterialTimePicker) {
        val hour = picker.hour
        val minute = if (picker.minute < 10) "0${picker.minute}" else picker.minute
        val amOrPm = if (picker.hour < 12) "AM" else "PM"
        val time = "$hour:$minute $amOrPm"
        binding.reminderTextView.text = time
    }

    private fun setColorClickListener() {
        val first = binding.firstColorCardView
        val second = binding.secondColorCardView
        val third = binding.thirdColorCardView
        val fourth = binding.fourthColorCardView
        val fifth = binding.fifthColorCardView
        val sixth = binding.sixthColorCardView
        val seventh = binding.seventhColorCardView
        val colorList = arrayListOf(first, second, third, fourth, fifth, sixth, seventh)
        val clickListener = View.OnClickListener { v ->
            when (v.id) {
                (first.id) -> {
                    selectColor(
                        first,
                        colorList.filter { color -> color != first }
                    )
                }
                (second.id) -> {
                    selectColor(
                        second,
                        colorList.filter { color -> color != second }
                    )
                }
                (third.id) -> {
                    selectColor(
                        third,
                        colorList.filter { color -> color != third }
                    )
                }
                (fourth.id) -> {
                    selectColor(
                        fourth,
                        colorList.filter { color -> color != fourth }
                    )
                }
                (fifth.id) -> {
                    selectColor(
                        fifth,
                        colorList.filter { color -> color != fifth }
                    )
                }
                (sixth.id) -> {
                    selectColor(
                        sixth,
                        colorList.filter { color -> color != sixth }
                    )
                }
                (seventh.id) -> {
                    selectColor(
                        seventh,
                        colorList.filter { color -> color != seventh }
                    )
                }
            }
        }
        for (color in colorList) {
            color.setOnClickListener(clickListener)
        }
    }

    private fun selectColor(card: MaterialCardView, colorList: List<MaterialCardView>) {
        card.strokeWidth = 8
        selectedColor = card.cardBackgroundColor.defaultColor
        disableColor(colorList)
    }

    private fun disableColor(colorList: List<MaterialCardView>) {
        for (color in colorList) {
            color.strokeWidth = 0
        }
    }

    private fun setDaysClickListener() {
        val sunday = binding.sundayCardView
        val monday = binding.mondayCardView
        val tuesday = binding.tuesdayCardView
        val wednesday = binding.wednesdayCardView
        val thursday = binding.thursdayCardView
        val friday = binding.fridayCardView
        val saturday = binding.saturdayCardView
        val dayList = arrayListOf(sunday, monday, tuesday, wednesday, thursday, friday, saturday)
        val clickListener = View.OnClickListener { v ->
            when (v.id) {
                (sunday.id) -> {
                    selectDay(sunday)
                    fab.isEnabled = isDataValid()
                }
                (monday.id) -> {
                    selectDay(monday)
                    fab.isEnabled = isDataValid()
                }
                (tuesday.id) -> {
                    selectDay(tuesday)
                    fab.isEnabled = isDataValid()
                }
                (wednesday.id) -> {
                    selectDay(wednesday)
                    fab.isEnabled = isDataValid()
                }
                (thursday.id) -> {
                    selectDay(thursday)
                    fab.isEnabled = isDataValid()
                }
                (friday.id) -> {
                    selectDay(friday)
                    fab.isEnabled = isDataValid()
                }
                (saturday.id) -> {
                    selectDay(saturday)
                    fab.isEnabled = isDataValid()
                }
            }
        }
        for (day in dayList) {
            day.setOnClickListener(clickListener)
        }
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

    private fun checkEnteredTitle() {
        binding.habitTitleTextView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                fab.isEnabled = isDataValid()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun isDataValid(): Boolean {
        return !isTitleEmpty() and getSelectedDays().isNotEmpty()
    }

    private fun getSelectedDays(): List<CardView> {
        val days = getDayCardViews()
        return days.filter { day ->
            day.tag.equals(Constants.SELECTED)
        }
    }

    private fun getDayCardViews(): ArrayList<CardView> {
        return arrayListOf(
            binding.sundayCardView,
            binding.mondayCardView,
            binding.tuesdayCardView,
            binding.wednesdayCardView,
            binding.thursdayCardView,
            binding.fridayCardView,
            binding.saturdayCardView
        )
    }

    private fun isTitleEmpty(): Boolean {
        val text = binding.habitTitleTextView.text.toString()
        return TextUtils.isEmpty(text)
    }

    private fun changeFabIconAnimation(icon: Int) {
        val fadeOutAnime =
            AnimationUtils.loadAnimation(requireContext(), R.anim.fade_out)
        val fadeInAnime =
            AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in)
        fab.startAnimation(fadeOutAnime)
        fadeOutAnime.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {}

            override fun onAnimationEnd(p0: Animation?) {
                fab.startAnimation(fadeInAnime)
                fab.setImageResource(icon)
            }

            override fun onAnimationRepeat(p0: Animation?) {}
        })
    }

    override fun onResume() {
        super.onResume()
        changeFabIconAnimation(R.drawable.ic_check)
    }

    override fun onStop() {
        super.onStop()
        if (!fab.isEnabled) fab.isEnabled = true
        changeFabIconAnimation(R.drawable.ic_add)
    }
}