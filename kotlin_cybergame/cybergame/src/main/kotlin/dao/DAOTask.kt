package com.hse.cyber.dao

import com.hse.cyber.config.DBConnection
import com.hse.cyber.constants.DBQueriesTask
import com.hse.cyber.constants.TaskFieldName
import com.hse.cyber.constants.TaskSolvedFieldName
import com.hse.cyber.model.NoDatabaseConnectionException
import com.hse.cyber.model.Task
import com.hse.cyber.model.TaskAlreadySolvedException
import com.hse.cyber.model.TaskNotFoundException
import com.hse.cyber.model.TaskRequest
import com.hse.cyber.model.TaskSolveRequest
import com.hse.cyber.model.UnexpectedResultException
import com.hse.cyber.model.UserNotFoundException
import com.hse.cyber.model.WrongTaskAnswerException
import com.hse.cyber.utills.Logger
import java.sql.Connection
import java.sql.ResultSet
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
        Logger.log(TAG, "getTaskList")
        val tasks = mutableListOf<Task>()
        connection.prepareStatement(DBQueriesTask.TASKS_GET_ALL).use { statement ->
            statement.executeQuery().use { resultSet ->
                while (resultSet.next()) {
                    val task = mapTask(resultSet)
                    Logger.log(TAG, "getTaskList add $task to list")
                    tasks.add(task)
                }
            }
        }
        Logger.log(TAG, "getTaskList final list: $tasks")
        return tasks
    }

    fun getSolvedTask(userId: Long): List<Task> {
        Logger.log(TAG, "getSolvedTask $userId")
        val solvedTaskIds = getSolvedTaskIds(userId)
        if (solvedTaskIds.isEmpty()) {
            return emptyList()
        }
        return getTaskList().filter { it.id in solvedTaskIds }
    }

    fun solveTask(request: TaskSolveRequest) {
        Logger.log(TAG, "solveTask userId=$request.userId taskId=$request.taskId")
        try {
            connection.prepareStatement(DBQueriesTask.TASK_SOLVE).use { statement ->
                statement.setLong(1, request.userId)
                statement.setLong(2, request.taskId)
                statement.setString(3, request.answer)
                statement.execute()
            }
        } catch (e: SQLException) {
            throw mapTaskSolve(e)
        }
    }

    fun deleteTask(taskId: Long) {
        Logger.log(TAG, "deleteTask $taskId")
        try {
            connection.prepareStatement(DBQueriesTask.TASK_DELETE).use { statement ->
                statement.setLong(1, taskId)
                statement.execute()
            }
        } catch (e: SQLException) {
            if (e.message?.contains("задача не найдена", ignoreCase = true) == true) {
                throw TaskNotFoundException()
            }
            throw e
        }
    }

    fun createTask(taskRequest: TaskRequest): Long {
        Logger.log(TAG, "createTask $taskRequest")
        connection.prepareStatement(DBQueriesTask.TASK_CREATE).use { statement ->
            statement.setString(1, taskRequest.header)
            statement.setString(2, taskRequest.description)
            statement.setString(3, taskRequest.answer)
            statement.setInt(4, taskRequest.points)
            statement.setString(5, taskRequest.hint ?: "")
            statement.executeQuery().use { resultSet ->
                if (resultSet.next()) {
                    return resultSet.getLong(1)
                }
                throw UnexpectedResultException()
            }
        }
    }

    private fun getSolvedTaskIds(userId: Long): Set<Long> {
        connection.prepareStatement(DBQueriesTask.TASK_GET_SOLVED).use { statement ->
            statement.setLong(1, userId)
            statement.executeQuery().use { resultSet ->
                if (resultSet.next()) {
                    val array = resultSet.getArray(TaskSolvedFieldName.TASK_ID)
                    val ids = array?.array as? Array<*>
                    return ids?.map { (it as Number).toLong() }?.toSet() ?: emptySet()
                }
            }
        }
        throw UserNotFoundException()
    }

    private fun mapTaskSolve(e: SQLException): Exception {
        val message = e.message.orEmpty()
        return when {
            message.contains("Неверный ответ", ignoreCase = true) -> WrongTaskAnswerException()
            message.contains("задача не найдена", ignoreCase = true) -> TaskNotFoundException()
            message.contains("пользователь не найден", ignoreCase = true) -> UserNotFoundException()
            message.contains("задача уже решена", ignoreCase = true) -> TaskAlreadySolvedException()
            else -> e
        }
    }

    private fun mapTask(resultSet: ResultSet): Task {
        return Task(
            id = resultSet.getLong(TaskFieldName.TASK_ID),
            header = resultSet.getString(TaskFieldName.HEADER),
            description = resultSet.getString(TaskFieldName.DESCRIPTION),
            answer = resultSet.getString(TaskFieldName.ANSWER),
            points = resultSet.getInt(TaskFieldName.POINTS),
            hint = resultSet.getString(TaskFieldName.HINT),
        )
    }

    private companion object {
        const val TAG = "DAOTask"
    }
}
