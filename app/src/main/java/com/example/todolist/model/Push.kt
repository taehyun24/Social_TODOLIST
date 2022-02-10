package com.example.todolist.model


data class Push(
    var to : String? = null,
    var notification : Notification = Notification()
){
    data class Notification(
        var body : String? = null,
        var title : String? = null
    )
}
