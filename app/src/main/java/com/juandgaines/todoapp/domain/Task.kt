package com.juandgaines.todoapp.domain

import java.time.LocalDateTime

data class Task(
    val id : String,
    val tittle :String,
    val description : String?,
    val isComplete :Boolean = false,
    val category : Category? = null,
    val date : LocalDateTime = LocalDateTime.now()
)

enum class Category{
    WORK,
    PERSONAL,
    SHOPPING,
    OTHER;

    companion object {
        fun fromOrdinal(ordinal: Int): Category? {
            return entries.find { it.ordinal == ordinal }
        }
    }
}



