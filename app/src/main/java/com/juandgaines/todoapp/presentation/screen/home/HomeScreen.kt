@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package com.juandgaines.todoapp.presentation.screen.home

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.juandgaines.todoapp.R
import com.juandgaines.todoapp.presentation.screen.home.components.SectionTittle
import com.juandgaines.todoapp.presentation.screen.home.components.SummaryInfo
import com.juandgaines.todoapp.presentation.screen.home.components.TaskItem
import com.juandgaines.todoapp.presentation.screen.home.providers.HomeScreenPreviewProvider
import com.juandgaines.todoapp.ui.theme.TodoAppTheme



@Composable
fun HomeScreenRoot(
    navigateToTaskScreen: (String?) -> Unit,
    viewModel: HomeScreenViewModel
){
    //val viewModel = viewModel<HomeScreenViewModel>()
    val state = viewModel.state
    val event = viewModel.event

    val context = LocalContext.current

    LaunchedEffect(true)
    {
        event.collect{ event ->
            when(event){
                HomeScreenEvent.DeleteAllTasks -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.all_Task_Deleted),
                        Toast.LENGTH_SHORT
                    )
                }
                HomeScreenEvent.DeleteTask -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.task_deleted),
                        Toast.LENGTH_SHORT
                    )
                }
                HomeScreenEvent.UpdateTastks -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.task_updated),
                        Toast.LENGTH_SHORT
                    )
                }
            }
        }

    }

    HomeScreen(
        state = state,
        onAction = { action ->
            when(action){
                is HomeScreenAction.OnAddTask ->  {
                    navigateToTaskScreen(null)
                }
                is HomeScreenAction.OnTaskClick -> {
                    navigateToTaskScreen(action.taskId)
                }
                else ->{
                    viewModel.onAction(action)
                }
            }
        }
    )
}

@Composable
fun HomeScreen(
    modifier : Modifier = Modifier,
    state : HomeDataState,
    onAction:(HomeScreenAction) -> Unit
) {
    var isMenuExpanded by remember { mutableStateOf(false) }
    Scaffold(
        modifier  = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxSize()
                    )
                },
                actions = {
                    Box(
                        modifier = Modifier.padding(8.dp).clickable {
                            isMenuExpanded = true
                        }
                    ){
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Add task",
                            tint = MaterialTheme.colorScheme.onSurface
                        )

                        DropdownMenu(
                            expanded = isMenuExpanded,
                            onDismissRequest = {
                                isMenuExpanded = false
                            }
                        )
                        {
                            DropdownMenuItem(
                                onClick = {
                                    onAction(HomeScreenAction.OnDeleteAll)
                                    isMenuExpanded = false
                                },
                                text = {
                                    Text(
                                        text = stringResource(id = R.string.delete_alll)
                                    )
                                }
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onAction(HomeScreenAction.OnAddTask)
                },
                content = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "add task",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            )
        }
    )
    { paddingValues ->

        if(state.completedTask.isEmpty() && state.pendingTask.isEmpty()){
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ){
                Text(
                    text = stringResource(R.string.no_tasks),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
        else {
            LazyColumn(
                modifier = Modifier.padding(paddingValues)
                    .padding(15.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                item {
                    SummaryInfo(
                        date = state.date,
                        taskSumarry = state.summary,
                        completedTask = state.completedTask.size,
                        totalTask = state.completedTask.size + state.pendingTask.size
                    )
                }

                stickyHeader {
                    SectionTittle(
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .background(
                                color = MaterialTheme.colorScheme.surface
                            ),
                        tittle = stringResource(R.string.pending_task)
                    )
                }

                items(
                    state.pendingTask,
                    key = {  task -> task.id }
                ){task ->
                    TaskItem(
                        modifier = Modifier.clip(
                            RoundedCornerShape(
                                8.dp
                            )
                        ),
                        task = task,
                        onClickItem = {
                            onAction(HomeScreenAction.OnTaskClick(task.id))
                        },
                        onDeleteItem = {
                            onAction(HomeScreenAction.OnDeleteTask(task))
                        },
                        onToggleCompletion = {
                            onAction(HomeScreenAction.OnToggleTask(it))
                        }
                    )
                }

                stickyHeader{
                    SectionTittle(
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .background(
                                color = MaterialTheme.colorScheme.surface
                            ),
                        tittle = stringResource(R.string.complete_task)
                    )
                }

                items(
                    items = state.completedTask,
                    key = { task -> task.id }
                ){ task ->
                    TaskItem(
                        modifier = Modifier
                            .clip(
                                RoundedCornerShape(8.dp)
                            )
                            .animateItem(),
                        task = task,
                        onClickItem = {
                            onAction(HomeScreenAction.OnTaskClick(task.id))
                        },
                        onDeleteItem = {
                            onAction(HomeScreenAction.OnDeleteTask(task))
                        },
                        onToggleCompletion = {
                            onAction(HomeScreenAction.OnToggleTask(task))
                        }
                    )
                }

            }
        }


    }
}

@Preview
@Composable
fun HomeScreenPreviewLight(
    @PreviewParameter(HomeScreenPreviewProvider::class) state: HomeDataState
) {
    TodoAppTheme {
        HomeScreen(
            state = HomeDataState(
                date = state.date,
                summary = state.summary,
                completedTask = state.completedTask,
                pendingTask =  state.pendingTask
            ),
            onAction = {}
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun HomeScreenPreviewDark(
    @PreviewParameter(HomeScreenPreviewProvider::class) state: HomeDataState
) {
    TodoAppTheme {
        HomeScreen(
            state = HomeDataState(
                date = state.date,
                summary = state.summary,
                completedTask = state.completedTask,
                pendingTask =  state.pendingTask
            ),
            onAction = {}
        )
    }
}


