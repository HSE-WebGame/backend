package com.hse.cyber.model

data class User(
    val id: Long?,
    val userName: String?,
    val userLogin: String?,
    val userPassword: String?,
    val secretWord: String?,
    val isAdmin: Boolean = false,
)