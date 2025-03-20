package com.juandgaines.todoapp.presentation.screen.home

import com.juandgaines.todoapp.domain.Task

sealed interface HomeScreenAction {
    data class OnTaskClick(val taskId : String) : HomeScreenAction
    data class OnToggleTask(val task : Task) :HomeScreenAction
    data class OnDeleteTask(val task :Task) :HomeScreenAction
    data object OnDeleteAll:HomeScreenAction
    data object OnAddTask : HomeScreenAction
}