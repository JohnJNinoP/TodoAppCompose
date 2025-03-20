package com.juandgaines.todoapp.presentation.screen.home.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.juandgaines.todoapp.ui.theme.TodoAppTheme

@Composable
fun SectionTittle(
    modifier: Modifier = Modifier,
    tittle : String
)
{
    Box(modifier = modifier){
        Text(
            text = tittle,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
fun SectionTittlePreview(){
    TodoAppTheme {
        SectionTittle(
            tittle = "Today"
        )
    }
}