package com.hse.cyber.dao

import com.hse.cyber.config.DBConnection
import com.hse.cyber.constants.AchievementFieldName
import com.hse.cyber.constants.DBQueriesAchievement
import com.hse.cyber.constants.DBQueriesTask
import com.hse.cyber.constants.DBQueriesUser
import com.hse.cyber.constants.UserAchievementFieldName
import com.hse.cyber.constants.UserFiledName
import com.hse.cyber.model.Achievement
import com.hse.cyber.model.AchievementGrantRequest
import com.hse.cyber.model.NoDatabaseConnectionException
import com.hse.cyber.model.User
import com.hse.cyber.model.UserAchievement
import com.hse.cyber.model.UserNotFoundException
import com.hse.cyber.utills.Logger
import java.sql.Connection
import java.sql.ResultSet
import java.sql.SQLException

class DAOAchievement {

    private val connection: Connection

    init {
        try {
            connection = DBConnection.getConnection() ?: throw NoDatabaseConnectionException()
        } catch (e: SQLException) {
            Logger.err(e)
            throw NoDatabaseConnectionException()
        }
    }

    fun getAchievementListByUserId(userId: Long): List<Achievement> {
        Logger.log(TAG, "getAchievementListByUserId")
        val achievements = mutableListOf<Achievement>()
        connection.prepareStatement(DBQueriesAchievement.ACHIEVEMENTS_GET).use { statement ->
            statement.setLong(1, userId)
            statement.executeQuery().use { resultSet ->
                while (resultSet.next()) {
                    val achievement = mapAchievement(resultSet)
                    Logger.log(TAG, "getAchievementListByUserId add $achievement to list")
                    achievements.add(achievement)
                }
            }
        }
        Logger.log(TAG, "getAchievementListByUserId final list: $achievements")
        return achievements
    }

    fun grantAchievement(achievementGrantRequest: AchievementGrantRequest) {
        Logger.log(TAG, "grantAchievement $achievementGrantRequest")
        connection.prepareStatement(DBQueriesAchievement.ACHIEVEMENTS_GET).use { statement ->
            statement.setLong(1, achievementGrantRequest.userId)
            statement.setLong(2, achievementGrantRequest.achievementId)
            statement.execute()
        }
        Logger.log(TAG, "grantAchievement userId:${achievementGrantRequest.userId} achievementId:${achievementGrantRequest.achievementId}")
    }

    private fun mapAchievement(resultSet: ResultSet): Achievement {
        return Achievement(
            id = resultSet.getLong(AchievementFieldName.ACHIEVEMENT_ID),
            header = resultSet.getString(AchievementFieldName.HEADER),
        )
    }

    private fun mapUserAchievement(resultSet: ResultSet): UserAchievement {
        return UserAchievement(
            userId = resultSet.getLong(UserAchievementFieldName.USER_ID),
            achievementListId = (resultSet.getArray(UserAchievementFieldName.ACHIEVEMENT_LIST_ID).array  as Array<*>)
                .filterIsInstance<Achievement>(),
        )
    }



    private companion object {
        const val TAG = "DAOAchievement"
    }
}