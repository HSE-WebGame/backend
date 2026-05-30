package com.hse.cyber.dao

import com.hse.cyber.config.DBConnection
import com.hse.cyber.model.Achievement
import com.hse.cyber.model.NoDatabaseConnection
import com.hse.cyber.utills.Logger
import java.sql.Connection
import java.sql.SQLException

class DAOAchievement {

    private val connection: Connection

    init {
        try {
            connection = DBConnection.getConnection() ?: throw NoDatabaseConnection()
        } catch (e: SQLException) {
            Logger.err("$e")
            throw NoDatabaseConnection()
        }
    }

    fun getAchievementListByUserId(userId: Long): List<Achievement> {

    }

    fun getAchievementIconByUrlCode(urlCode: String): String {

    }
}