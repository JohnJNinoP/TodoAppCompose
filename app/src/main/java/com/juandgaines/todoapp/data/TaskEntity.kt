package com.juandgaines.todoapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.juandgaines.todoapp.domain.Category
import com.juandgaines.todoapp.domain.Task
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val title: String,
    val description: String,
    @ColumnInfo(name = "is_completed")
    val isComplete: Boolean,
    val category:Int? = null,
    val date :Long
) {
    companion object {
        fun fromTask(task: Task): TaskEntity {
            return TaskEntity(
                id = task.id,
                title = task.tittle,
                description = task.description ?: "",
                isComplete = task.isComplete,
                category = task.category?.ordinal,
                date = task.date.atZone(
                    ZoneId.systemDefault()
                ).toInstant().toEpochMilli()
            )
        }
    }

    fun toTask(): Task {
        return Task(
            id = id,
            tittle = title,
            description = description,
            isComplete = isComplete,
            category = category?.let {
                Category.fromOrdinal(it)
            },
            date = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(date),
                ZoneId.systemDefault()
            )
        )
    }
}