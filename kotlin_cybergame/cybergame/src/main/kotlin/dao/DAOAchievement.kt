package com.hse.cyber.dao

import com.hse.cyber.config.DBConnection
import com.hse.cyber.model.Achievement
import com.hse.cyber.model.NoDatabaseConnectionException
import com.hse.cyber.utills.Logger
import java.sql.Connection
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

    }

    fun getAchievementIconByUrlCode(urlCode: String): String {

    }

    private companion object {
        const val TAG = "DAOAchievement"
    }
}