package com.hse.cyber.dao

import com.hse.cyber.config.DBConnection
import com.hse.cyber.model.NoDatabaseConnectionException
import com.hse.cyber.model.Task
import com.hse.cyber.model.TaskRequest
import com.hse.cyber.utills.Logger
import java.sql.Connection
import java.sql.SQLException

class DAOTask {

    private val connection: Connection

    init {
        try {
            connection = DBConnection.getConnection() ?: throw NoDatabaseConnectionException()
        } catch (e: SQLException) {
            Logger.err(e)
            throw NoDatabaseConnectionException()
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

    private companion object {
        const val TAG = "DAOTask"
    }
}