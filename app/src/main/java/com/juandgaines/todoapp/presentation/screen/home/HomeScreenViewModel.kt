package com.juandgaines.todoapp.presentation.screen.home

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
import com.juandgaines.todoapp.TodoApplication
import com.juandgaines.todoapp.data.FakeTaskLocalDataSource
import com.juandgaines.todoapp.domain.TaskLocalDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val taskLocalDataSource: TaskLocalDataSource

) : ViewModel() {
    //private val taskLocalDataSource = FakeTaskLocalDataSource

    var state by mutableStateOf(HomeDataState())
        private set

    private val eventChannel = Channel<HomeScreenEvent>()
    val event = eventChannel.receiveAsFlow()

    init  {

        state = state.copy(
            date = LocalDate.now().let {
                DateTimeFormatter.ofPattern("EEE, MMMM dd yyyy").format(it)
            }
        )

        taskLocalDataSource.taskFlow
            .onEach {
                val completedTask = it
                    .filter {task -> task.isComplete}
                    .sortedByDescending { task -> task.date }
                val pendingTask = it
                    .filter{ task -> !task.isComplete }
                    .sortedByDescending { task -> task.date }


                state = state.copy(
                    summary = pendingTask.size.toString(),
                    completedTask = completedTask,
                    pendingTask = pendingTask
                )
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action:HomeScreenAction){
        viewModelScope.launch {
            when(action){
                HomeScreenAction.OnDeleteAll -> {
                    taskLocalDataSource.deleteAllTask()
                    eventChannel.send(HomeScreenEvent.DeleteAllTasks)
                }
                is HomeScreenAction.OnDeleteTask -> {
                    taskLocalDataSource.removeTask(action.task)
                    eventChannel.send(HomeScreenEvent.DeleteTask)
                }

                is HomeScreenAction.OnToggleTask -> {
                    val updateTask = action.task.copy(isComplete =  !action.task.isComplete)
                    taskLocalDataSource.updateTask(updateTask)
                    eventChannel.send(HomeScreenEvent.UpdateTastks)

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
//                HomeScreenViewModel(
//                    taskLocalDataSource = dataSource,
//                    savedStateHandle = savedStateHandle
//                )
//            }
//        }
//    }

}