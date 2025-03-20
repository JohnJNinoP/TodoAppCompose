package com.juandgaines.todoapp.presentation.screen.home.providers


import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.juandgaines.todoapp.domain.Category
import com.juandgaines.todoapp.domain.Task
import com.juandgaines.todoapp.presentation.screen.home.HomeDataState

class HomeScreenPreviewProvider : PreviewParameterProvider<HomeDataState> {
    override val values: Sequence<HomeDataState>
        get() = sequenceOf(
            HomeDataState(
                date = "March 10, 2025",
                summary = "5 incomplete, 5 completed",
                completedTask = completeTask,
                pendingTask = pendingTask
            )
        )
}

val completeTask = mutableListOf<Task>()
    .apply{
        repeat(5){
            add(
                Task(
                    id = it.toString(),
                    tittle = "Task $it",
                    description = "Description $it",
                    category = Category.WORK,
                    isComplete = false
                )
            )
        }
    }

val pendingTask = mutableListOf<Task>()
    .apply {
        repeat(8){
            add(
                Task(
                    id = (it+30).toString(),
                    tittle = "Task $it",
                    description = "Description $it",
                    category = Category.OTHER,
                    isComplete = true
                )
            )
        }
    }