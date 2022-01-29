package com.example.todolist.model

data class Memo(
    var name: String? = null,
    var time: String? = null,
    var uid: String? = null,
    var date: String? = null,
    var userEmail: String? = null,
    var timestamp: Long? = null,
    var cheerupCount : Int = 0,
    var cheerup: MutableMap<String,Boolean> = HashMap(),
    var check_status: Boolean = false,
) {
}