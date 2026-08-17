package com.momentum.habit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.momentum.habit.ui.HabitScreen
import com.momentum.habit.ui.HabitViewModel
import com.momentum.habit.ui.theme.MomentumTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MomentumTheme {
                val viewModel: HabitViewModel = viewModel()
                HabitScreen(viewModel)
            }
        }
    }
}