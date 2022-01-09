package com.example.recyclerview

data class TodoModel(
    val id: Int? = 0,
    val title: String,
    val text: String? = null,
    var isDone: Int = 0,
)