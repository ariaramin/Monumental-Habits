package com.ariaramin.monumentalhabits.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.ariaramin.monumentalhabits.R
import com.ariaramin.monumentalhabits.databinding.FragmentAddHabitBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton


class AddHabitFragment : Fragment() {

    private lateinit var binding: FragmentAddHabitBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddHabitBinding.inflate(inflater, container, false)
        changeFloatingActionButtonIcon(R.drawable.ic_check)
        setDaysClickListener()

        return binding.root
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
        if (day.cardBackgroundColor.defaultColor == primaryColor)
            day.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.secondaryColorLight
                )
            )
        else {
            day.setCardBackgroundColor(primaryColor)
        }
    }

    private fun changeFloatingActionButtonIcon(icon: Int) {
        val fab = requireActivity().findViewById<FloatingActionButton>(R.id.fab)
        fab.setImageResource(icon)
    }

    override fun onDestroy() {
        super.onDestroy()
        changeFloatingActionButtonIcon(R.drawable.ic_add)
    }
}