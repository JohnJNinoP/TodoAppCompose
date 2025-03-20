package com.juandgaines.todoapp.presentation.screen.home

sealed class HomeScreenEvent {
    data object UpdateTastks :HomeScreenEvent()
    data object  DeleteAllTasks:HomeScreenEvent()
    data object  DeleteTask :HomeScreenEvent()
}