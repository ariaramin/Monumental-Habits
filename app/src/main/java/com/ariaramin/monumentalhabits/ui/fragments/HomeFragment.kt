package com.ariaramin.monumentalhabits.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ariaramin.monumentalhabits.Adapter.HabitAdapter
import com.ariaramin.monumentalhabits.Calendar.SingleRowCalendarManager
import com.ariaramin.monumentalhabits.MainViewModel
import com.ariaramin.monumentalhabits.Models.Habit
import com.ariaramin.monumentalhabits.R
import com.ariaramin.monumentalhabits.Utils.Constants
import com.ariaramin.monumentalhabits.databinding.FragmentHomeBinding
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.michalsvec.singlerowcalendar.calendar.CalendarChangesObserver
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(), CalendarChangesObserver {

    private lateinit var binding: FragmentHomeBinding

    @Inject
    lateinit var singleRowCalendarManager: SingleRowCalendarManager
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        visibleBottomAppbar()
        singleRowCalendarManager.getCalendar(
            binding.singleRowCalendar,
            Constants.CALENDAR_TAG,
            null,
            null,
            this
        )
        observeHabits()

        return binding.root
    }

    private fun observeHabits() {
        mainViewModel.habitList.observe(requireActivity()) { habits ->
            showDataInRecyclerView(habits)
        }
    }

    private fun showDataInRecyclerView(habitList: List<Habit>) {
        if (binding.habitsRecyclerView.adapter == null) {
            val adapter = HabitAdapter(habitList, singleRowCalendarManager)
            binding.habitsRecyclerView.adapter = adapter
        } else {
            val adapter = binding.habitsRecyclerView.adapter as HabitAdapter
            adapter.updateList(habitList)
        }
    }

    override fun whenCalendarScrolled(dx: Int, dy: Int) {
        super.whenCalendarScrolled(dx, dy)
        val recyclerView = binding.habitsRecyclerView
        for (i in recyclerView.children) {
            val holder = recyclerView.getChildViewHolder(i) as HabitAdapter.HabitViewHolder
            val contributionCalendar = holder.binding.contributionCalendar
            contributionCalendar.suppressLayout(false)
            contributionCalendar.scrollBy(dx, 0)
            contributionCalendar.suppressLayout(true)
        }
    }

    private fun visibleBottomAppbar() {
        val bottomAppBar =
            requireActivity().findViewById<BottomAppBar>(R.id.bottomAppBar)
        val fab =
            requireActivity().findViewById<FloatingActionButton>(R.id.fab)
        setUpFabNavigation(fab)
        bottomAppBar.visibility = View.VISIBLE
        fab.visibility = View.VISIBLE
    }

    private fun setUpFabNavigation(fab: FloatingActionButton) {
        fab.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addHabitFragment)
        }
    }
}