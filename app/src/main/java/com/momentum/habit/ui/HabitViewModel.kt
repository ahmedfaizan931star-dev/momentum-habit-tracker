package com.momentum.habit.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.momentum.habit.data.Habit
import com.momentum.habit.data.HabitDatabase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

class HabitViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = HabitDatabase.getDatabase(application).habitDao()

    val habits: StateFlow<List<Habit>> = dao.getAllHabits().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun addHabit(name: String, description: String) {
        viewModelScope.launch {
            dao.insertHabit(Habit(name = name, description = description))
        }
    }

    fun toggleHabit(habit: Habit) {
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            val calendar = Calendar.getInstance()
            val today = calendar.get(Calendar.DAY_OF_YEAR)
            
            val lastCompleted = habit.lastCompletedAt?.let {
                calendar.timeInMillis = it
                calendar.get(Calendar.DAY_OF_YEAR)
            }

            if (lastCompleted != today) {
                val newStreak = if (lastCompleted == today - 1) habit.streak + 1 else 1
                dao.updateHabit(habit.copy(streak = newStreak, lastCompletedAt = now))
            }
        }
    }

    fun deleteHabit(habit: Habit) {
        viewModelScope.launch {
            dao.deleteHabit(habit)
        }
    }
}