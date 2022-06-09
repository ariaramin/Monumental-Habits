package com.ariaramin.monumentalhabits.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.ariaramin.monumentalhabits.Calendar.SingleRowCalendarManager
import com.ariaramin.monumentalhabits.R
import com.ariaramin.monumentalhabits.databinding.FragmentHomeBinding
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.michalsvec.singlerowcalendar.calendar.CalendarChangesObserver
import com.michalsvec.singlerowcalendar.calendar.CalendarViewManager
import com.michalsvec.singlerowcalendar.calendar.SingleRowCalendarAdapter
import com.michalsvec.singlerowcalendar.selection.CalendarSelectionManager
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(), CalendarChangesObserver {

    private lateinit var binding: FragmentHomeBinding

    @Inject
    lateinit var singleRowCalendarManager: SingleRowCalendarManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        visibleBottomAppbar()
        singleRowCalendarManager.getCalendar(binding.singleRowCalendar, this)

        return binding.root
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