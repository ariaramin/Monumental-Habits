package com.ariaramin.monumentalhabits.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.ariaramin.monumentalhabits.Calendar.SingleRowCalendarManager
import com.ariaramin.monumentalhabits.MainViewModel
import com.ariaramin.monumentalhabits.Models.Habit
import com.ariaramin.monumentalhabits.Utils.Constants
import com.ariaramin.monumentalhabits.databinding.FragmentSettingsBinding
import com.michalsvec.singlerowcalendar.calendar.CalendarChangesObserver
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : Fragment(), CalendarChangesObserver {

    private lateinit var binding: FragmentSettingsBinding

    @Inject
    lateinit var singleRowCalendarManager: SingleRowCalendarManager
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        getHabits()

        return binding.root
    }

    private fun getHabits() {
        mainViewModel.habitList.observe(requireActivity()) { habits ->
            setupHabitTracker(habits)
        }
    }

    private fun setupHabitTracker(habits: List<Habit>) {
        singleRowCalendarManager.getCalendar(
            binding.habitTrackerCalendar,
            Constants.TRACK_HABITS_TAG,
            null,
            habits,
            this
        )
    }

}