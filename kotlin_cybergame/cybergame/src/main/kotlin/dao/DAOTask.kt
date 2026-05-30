package com.hse.cyber.dao

import com.hse.cyber.config.DBConnection
import com.hse.cyber.model.NoDatabaseConnection
import com.hse.cyber.model.Task
import com.hse.cyber.model.TaskRequest
import com.hse.cyber.utills.Logger
import java.sql.Connection
import java.sql.SQLException

class DAOTask {

    private val connection: Connection

    init {
        try {
            connection = DBConnection.getConnection() ?: throw NoDatabaseConnection()
        } catch (e: SQLException) {
            Logger.err("$e")
            throw NoDatabaseConnection()
        }
    }

    fun getTaskList(): List<Task> {

    }

    fun getSolvedTask(userId: Long): List<Task> {

    }

    fun solveTask(taskId: Long) {

    }

    fun deleteTask(taskId: Long) {

    }

    fun createTask(taskRequest: TaskRequest) {

    }
}