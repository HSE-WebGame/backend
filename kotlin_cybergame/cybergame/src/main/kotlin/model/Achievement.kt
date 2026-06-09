package com.hse.cyber.model

data class Achievement(
    val id: Long,
    val header: String,
)

data class AchievementGrantRequest(
    val achievementId: Long,
    val userId: Long,
)

data class UserAchievement(
    val userId: Long,
    val achievementListId: List<Achievement>
)