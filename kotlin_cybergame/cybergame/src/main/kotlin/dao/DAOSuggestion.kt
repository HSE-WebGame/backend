package com.hse.cyber.dao

import com.hse.cyber.config.DBConnection
import com.hse.cyber.constants.DBQueriesSuggestion
import com.hse.cyber.constants.DBQueriesTask
import com.hse.cyber.constants.DBQueriesUser
import com.hse.cyber.constants.SuggestionFieldName
import com.hse.cyber.model.DuplicateUserException
import com.hse.cyber.model.InvalidFormatException
import com.hse.cyber.model.NoDatabaseConnectionException
import com.hse.cyber.model.Suggestion
import com.hse.cyber.model.SuggestionNotFoundException
import com.hse.cyber.model.SuggestionRequest
import com.hse.cyber.model.Task
import com.hse.cyber.model.TaskNotFoundException
import com.hse.cyber.model.UnexpectedResultException
import com.hse.cyber.utills.Logger
import com.hse.cyber.utills.UserFieldValidator.Companion.validateUserFieldsRegister
import java.sql.Connection
import java.sql.ResultSet
import java.sql.SQLException
import kotlin.Long

class DAOSuggestion {

    private val connection: Connection

    init {
        try {
            connection = DBConnection.getConnection() ?: throw NoDatabaseConnectionException()
        } catch (e: SQLException) {
            Logger.err(e)
            throw NoDatabaseConnectionException()
        }
    }

    fun getSuggestionList(): List<Suggestion> {
        Logger.log(TAG, "getTaskList")
        val suggests = mutableListOf<Suggestion>()
        connection.prepareStatement(DBQueriesSuggestion.SUGGESTION_GET).use { statement ->
            statement.executeQuery().use { resultSet ->
                while (resultSet.next()) {
                    val suggest = mapSuggestion(resultSet)
                    Logger.log(TAG, "getSuggestionList add $suggest to list")
                    suggests.add(suggest)
                }
            }
        }
        Logger.log(TAG, "getSuggestionList final list: $suggests")
        return suggests
    }

    fun createSuggestion(suggestionRequest: SuggestionRequest): Long {
        Logger.log(TAG, "createSuggestion $suggestionRequest")
        connection.prepareStatement(DBQueriesSuggestion.SUGGESTION_CRETE).use { statement ->
            statement.setString(1, suggestionRequest.header)
            statement.setString(2, suggestionRequest.description)
            statement.setString(3, suggestionRequest.answer)
            statement.setInt(4, suggestionRequest.points)
            statement.setLong(5, suggestionRequest.authorId)
            statement.setString(6, suggestionRequest.hint)
            statement.executeQuery().use { resultSet ->
                if (resultSet.next()) {
                    return resultSet.getLong(1)
                } else {
                    throw UnexpectedResultException()
                }
            }
        }
    }

    fun deleteSuggestion(suggestId: Long) {
        Logger.log(TAG, "deleteSuggestion $suggestId")
        try {
            connection.prepareStatement(DBQueriesSuggestion.SUGGESTION_DELETE).use { statement ->
                statement.setLong(1, suggestId)
                statement.execute()
            }
        } catch (e: SQLException) {
            if (e.message?.contains("Предложение не найдено", ignoreCase = true) == true) {
                throw SuggestionNotFoundException()
            }
            throw e
        }
    }

    private fun mapSuggestion(resultSet: ResultSet): Suggestion {
        return Suggestion(
            id = resultSet.getLong(SuggestionFieldName.SUGGESTION_ID),
            header = resultSet.getString(SuggestionFieldName.HEADER),
            description = resultSet.getString(SuggestionFieldName.DESCRIPTION),
            answer = resultSet.getString(SuggestionFieldName.ANSWER),
            points = resultSet.getInt(SuggestionFieldName.POINTS),
            hint = resultSet.getString(SuggestionFieldName.HINT),
            authorId = resultSet.getLong(SuggestionFieldName.AUTHOR_ID),
        )
    }


    private companion object {
        const val TAG = "DAOSuggestion"
    }
}