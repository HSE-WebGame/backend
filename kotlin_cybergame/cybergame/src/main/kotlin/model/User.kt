package com.hse.cyber.model

data class User(
    val id: Long,
    val name: String,
    val login: String,
    val password: String,
    val secretWord: String,
    val isAdmin: Boolean = false,
)

data class UserAuth(
    val login: String,
    val password: String,
)

data class UserRegister(
    val name: String,
    val login: String,
    val password: String,
    val secretWord: String,
)