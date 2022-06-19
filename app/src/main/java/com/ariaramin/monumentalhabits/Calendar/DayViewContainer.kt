package com.ariaramin.monumentalhabits.Calendar

import android.view.View
import com.ariaramin.monumentalhabits.databinding.CalendarDayLayoutBinding
import com.kizitonwose.calendarview.ui.ViewContainer

class DayViewContainer(view: View) : ViewContainer(view) {
    val itemView = CalendarDayLayoutBinding.bind(view)
}