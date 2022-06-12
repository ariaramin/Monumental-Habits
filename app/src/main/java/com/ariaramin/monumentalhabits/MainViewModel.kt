package com.ariaramin.monumentalhabits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ariaramin.monumentalhabits.Models.Habit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    fun insertHabit(habit: Habit) {
        viewModelScope.launch {
            mainRepository.insertHabit(habit)
        }
    }
}