package com.hse.cyber.constants

object DBQueriesAchievement {
    const val ACHIEVEMENTS_GET = "SELECT * FROM achievements_get(1);"
    const val ACHIEVEMENT_GRANT = "CALL achievement_grant(1, 1);"
}