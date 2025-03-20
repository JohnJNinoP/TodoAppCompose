package com.juandgaines.todoapp.domain

import kotlinx.coroutines.flow.Flow

interface TaskLocalDataSource  {
    val taskFlow : Flow<List<Task>>
    suspend fun addTask(task: Task)
    suspend fun updateTask(task: Task)
    suspend fun removeTask(task: Task)
    suspend fun deleteAllTask()
    suspend fun getTaskId(id: String) : Task?


}