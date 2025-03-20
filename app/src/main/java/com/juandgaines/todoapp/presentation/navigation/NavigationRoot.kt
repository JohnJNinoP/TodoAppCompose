package com.juandgaines.todoapp.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.juandgaines.todoapp.presentation.screen.detail.TaskScreenRoot
import com.juandgaines.todoapp.presentation.screen.detail.TaskViewModel
import com.juandgaines.todoapp.presentation.screen.home.HomeScreenRoot
import com.juandgaines.todoapp.presentation.screen.home.HomeScreenViewModel
import kotlinx.serialization.Serializable


@Composable
fun NavigationRoot(
    navController: NavHostController
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ){
        NavHost(
            navController = navController,
            startDestination = HomeScreenDes
        ){
            composable<HomeScreenDes>{
                val homeViewModel : HomeScreenViewModel = hiltViewModel()

                HomeScreenRoot(
                    navigateToTaskScreen = {
                        navController.navigate(TaskScreenDes(it))
                    },
                    viewModel = homeViewModel
                )
            }

            composable<TaskScreenDes> {
                val taskViewModel : TaskViewModel = hiltViewModel()
                TaskScreenRoot(
                    viewModel = taskViewModel,
                    navigateBack = {
                        navController.navigateUp()
                    }
                )
            }
        }
    }
}

@Serializable
object HomeScreenDes

@Serializable
data class TaskScreenDes(
    val taskId : String? = null
)