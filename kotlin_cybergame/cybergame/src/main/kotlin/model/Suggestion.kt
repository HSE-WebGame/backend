package com.hse.cyber.model

data class Suggestion(
    val id: Long,
    val header: String,
    val description: String,
    val answer: String,
    val points: Int,
    val hint: String?,
    val authorId: Long,
)

data class SuggestionRequest(
    val header: String,
    val description: String,
    val answer: String,
    val points: Int,
    val hint: String?,
    val authorId: Long,
)