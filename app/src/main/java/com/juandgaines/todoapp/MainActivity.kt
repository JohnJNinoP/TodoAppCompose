package com.juandgaines.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.juandgaines.todoapp.domain.Task
import com.juandgaines.todoapp.presentation.navigation.NavigationRoot
import com.juandgaines.todoapp.presentation.screen.detail.TaskScreenRoot
import com.juandgaines.todoapp.presentation.screen.home.HomeScreenRoot
import com.juandgaines.todoapp.ui.theme.TodoAppTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TodoAppTheme() {

                val navController = rememberNavController()
                //HelloTess()
                //HelloWorldView()
                //MainActivityTest()
                // HomeScreenRoot()
                NavigationRoot(
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun MainActivityTest(){
    var text by remember {
        mutableStateOf("")
    }

    val fakeLocalDataSource = com.juandgaines.todoapp.data.FakeTaskLocalDataSource

    LaunchedEffect(true) {
        fakeLocalDataSource.addTask(
            Task(
                id = UUID.randomUUID().toString(),
                tittle = "Task 1",
                description = "description 1"
            )
        )
    }

    LaunchedEffect(true){
        fakeLocalDataSource.taskFlow.collect{
            text = it.toString()
        }
    }



    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Text(
            text = text ,
            modifier = Modifier
                .padding(
                    top = innerPadding.calculateTopPadding()
                )
                .fillMaxSize()

        )
    }
}

@Composable
fun HelloTess(){
    var isShown by remember { mutableStateOf(false) }

    Column (
        modifier = Modifier.padding(56.dp)
    ) {
        if (isShown) {
            Text("This is a message")
        }
        Text(
            "Hello, world!",
            modifier = Modifier.clickable {
                isShown = !isShown
            }
        )
    }
}


@Composable
fun HelloWorldView() {
    var isShown by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.padding(56.dp)
    ) {
        MessageText(isShown)
        HelloWorldText {
            isShown = !isShown
        }
    }
}

@Composable
fun MessageText(isShown: Boolean) {
    if (isShown) {
        Text("This is a message")
    }
}

@Composable
fun HelloWorldText(onClick: () -> Unit) {
    Text(
        "Hello, world!",
        modifier = Modifier.clickable {
            onClick()
        }
    )
}


