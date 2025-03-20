package com.juandgaines.todoapp.data

import com.juandgaines.todoapp.domain.Task
import com.juandgaines.todoapp.domain.TaskLocalDataSource
import com.juandgaines.todoapp.presentation.screen.home.providers.completeTask
import com.juandgaines.todoapp.presentation.screen.home.providers.pendingTask
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

object FakeTaskLocalDataSource : TaskLocalDataSource
{
    private val _taskFlow = MutableStateFlow<List<Task>>(emptyList())

    init{
        _taskFlow.value = completeTask + pendingTask
    }

    override val taskFlow: Flow<List<Task>>
        get() = _taskFlow

    override suspend fun addTask(task: Task) {
        val tasks = _taskFlow.value.toMutableList()
        tasks.add(task)
        delay(100)
        _taskFlow.value = tasks

    }

    override suspend fun updateTask(task: Task) {
        val tasks = _taskFlow.value.toMutableList()
        val taskIndex = tasks.indexOfFirst{ it.id  == task.id }
        if(taskIndex != -1){
            tasks[taskIndex] = task
            delay(100)
            _taskFlow.value = tasks
        }

    }

    override suspend fun removeTask(task: Task) {
        val tasks = _taskFlow.value.toMutableList()
        val taskIndex = tasks.indexOfFirst { it.id   == task.id }
        if(taskIndex != -1){
            tasks.removeAt(taskIndex)
            delay(100)
            _taskFlow.value = tasks
        }
    }

    override suspend fun deleteAllTask() {
        _taskFlow.value = emptyList()
    }

    override suspend fun getTaskId(id: String): Task? {
        return  _taskFlow.value.firstOrNull{ it.id == id }
    }
}