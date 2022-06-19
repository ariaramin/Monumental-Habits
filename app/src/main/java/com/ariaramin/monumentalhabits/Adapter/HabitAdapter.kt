package com.ariaramin.monumentalhabits.Adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.ariaramin.monumentalhabits.Calendar.SingleRowCalendarManager
import com.ariaramin.monumentalhabits.Models.Habit
import com.ariaramin.monumentalhabits.R
import com.ariaramin.monumentalhabits.Utils.Constants
import com.ariaramin.monumentalhabits.databinding.HabitsItemLayoutBinding
import com.michalsvec.singlerowcalendar.calendar.CalendarChangesObserver

class HabitAdapter(
    private var habitList: List<Habit>,
    private val singleRowCalendarManager: SingleRowCalendarManager
) : RecyclerView.Adapter<HabitAdapter.HabitViewHolder>() {

    private lateinit var binding: HabitsItemLayoutBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = HabitsItemLayoutBinding.inflate(inflater, parent, false)
        binding = view
        return HabitViewHolder(view)
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        holder.bindData(habitList[position], singleRowCalendarManager)
    }

    override fun getItemCount(): Int {
        return habitList.size
    }

    fun updateList(list: List<Habit>) {
        habitList = list
        notifyDataSetChanged()
    }

    inner class HabitViewHolder(val binding: HabitsItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root), CalendarChangesObserver {

        fun bindData(habit: Habit, singleRowCalendarManager: SingleRowCalendarManager) {
            binding.habitTitleTextView.text = habit.title
            singleRowCalendarManager.getCalendar(
                binding.contributionCalendar,
                Constants.CONTRIBUTION_TAG,
                habit,
                this
            )
            binding.contributionCalendar.suppressLayout(true)
            binding.root.setOnClickListener { view ->
                val bundle = Bundle()
                bundle.putParcelable(Constants.HABIT, habit)
                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_habitFragment, bundle)
            }
        }
    }

}