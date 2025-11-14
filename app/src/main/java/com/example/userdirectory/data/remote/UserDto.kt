package com.example.userdirectory.data.remote

data class UserDto(
    val id: Int,
    val name: String,
    val username: String,
    val email: String,
    val phone: String,
    val website: String
)
