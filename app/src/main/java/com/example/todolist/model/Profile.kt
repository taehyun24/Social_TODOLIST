package com.example.todolist.model

data class Profile(
    var email: String? = null,
    var uid: String? = null,
    var nickName: String? = null,
    var gauge_value: Int = 0,
    var crown_color: String = "bronze",
    var followerCount: Int = 0,
    var followers: MutableMap<String, Boolean> = HashMap(),
    var followingCount: Int = 0,
    var followings: MutableMap<String, Boolean> = HashMap(),
)