package com.ariaramin.monumentalhabits

import androidx.lifecycle.MutableLiveData
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

    val habitList = MutableLiveData<List<Habit>>()

    init {
        readHabits()
    }

    fun insertHabit(habit: Habit) {
        viewModelScope.launch {
            mainRepository.insertHabit(habit)
        }
    }

    private fun readHabits() {
        viewModelScope.launch {
            try {
                val habits = mainRepository.readHabits()
                habitList.postValue(habits)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateHabit(habit: Habit) {
        viewModelScope.launch {
            mainRepository.updateHabit(habit)
        }
    }

}