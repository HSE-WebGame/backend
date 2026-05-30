package com.hse.cyber.model

data class Achievement(
    val id: Long,
    val header: Long,
    val urlCode: Int,
)

data class AchievementIcon(
    val urlCode: Int,
    val icon: String,
)