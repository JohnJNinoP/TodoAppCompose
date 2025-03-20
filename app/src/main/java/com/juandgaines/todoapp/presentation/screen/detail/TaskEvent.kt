package com.juandgaines.todoapp.presentation.screen.detail

sealed interface TaskEvent{
    data object TaskCreated: TaskEvent
}