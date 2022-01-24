package com.example.todolist.model

data class Profile(
    var email: String? = null,
    var uid: String? = null,
    var gauge_value: Int = 0,
    var crown_color: String = "bronze",
)