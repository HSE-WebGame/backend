package com.hse.cyber.model

data class Task (
    val id: Long,
    val header: String,
    val hint: String?,
    val description: String,
    val answer: String,
    val points: Int,
)

data class TaskRequest(
    val header: String,
    val hint: String?,
    val description: String,
    val answer: String,
    val points: Int,
)

data class TaskSolveRequest(
    val userId: Long,
    val taskId: Long,
    val answer: String,
)