package com.juandgaines.todoapp.presentation.screen.detail

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.toRoute
import com.juandgaines.todoapp.TodoApplication
import com.juandgaines.todoapp.data.FakeTaskLocalDataSource
import com.juandgaines.todoapp.domain.Task
import com.juandgaines.todoapp.domain.TaskLocalDataSource
import com.juandgaines.todoapp.presentation.navigation.TaskScreenDes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val localDataSource: TaskLocalDataSource
) : ViewModel() {

    //private val fakeTaskLocalDataSource = FakeTaskLocalDataSource

    var state by mutableStateOf(TaskScreenState())
        private set

    private var eventChannel = Channel<TaskEvent>()
    val event = eventChannel.receiveAsFlow()
    private val canSaveTask = snapshotFlow { state.taskName.text.toString() }

    val taskData = savedStateHandle.toRoute<TaskScreenDes>()
    private var editTask :Task? = null


    init {

        taskData.taskId?.let {
            viewModelScope.launch {
                val task = localDataSource.getTaskId( it)
                editTask = task

                state = state.copy(
                    taskName = TextFieldState(editTask?.tittle?:""),
                    taskDescription = TextFieldState(editTask?.description?:""),
                    isTaskDone = editTask?.isComplete?:false,
                    category = editTask?.category
                )
            }
        }

        canSaveTask.onEach {
            state = state.copy(canSaveTask = it.isNotEmpty())
        }.launchIn(viewModelScope)
    }

    fun onAction(actionTask : ActionTask){
        viewModelScope.launch {
            when(actionTask)
            {
                is ActionTask.ChangeTaskCategory -> {
                    state = state.copy(
                        category = actionTask.category
                    )
                }
                is ActionTask.ChangeTaskDone -> {
                    state = state.copy(
                        isTaskDone =  actionTask.isTaskDone
                    )
                }
                ActionTask.SaveTask -> {

                    editTask?.let {
                        localDataSource.updateTask(
                            task = it.copy(
                                id = it.id,
                                tittle = state.taskName.text.toString(),
                                description = state.taskDescription.text.toString(),
                                isComplete = state.isTaskDone,
                                category = state.category
                            )
                        )
                    }?:run{
                        val task= Task(
                            id = UUID.randomUUID().toString(),
                            tittle = state.taskName.text.toString(),
                            description = state.taskDescription.text.toString(),
                            isComplete = state.isTaskDone,
                            category = state.category
                        )
                        localDataSource.addTask(
                            task = task
                        )
                    }


                    eventChannel.send(TaskEvent.TaskCreated)
                }
                else -> Unit
            }
        }

    }

//    companion object{
//        val Factory: ViewModelProvider.Factory = viewModelFactory {
//            initializer {
//                val savedStateHandle = createSavedStateHandle()
//                val dataSource = (this[APPLICATION_KEY] as TodoApplication).dataSource
//                TaskViewModel(
//                    localDataSource = dataSource,
//                    savedStateHandle = savedStateHandle
//                )
//            }
//        }
//    }
}