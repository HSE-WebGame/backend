package com.hse.cyber.dao

import com.hse.cyber.config.DBConnection
import com.hse.cyber.model.NoDatabaseConnectionException
import com.hse.cyber.model.Suggestion
import com.hse.cyber.model.SuggestionRequest
import com.hse.cyber.model.Task
import com.hse.cyber.utills.Logger
import java.sql.Connection
import java.sql.SQLException

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

    }

    fun createSuggestion(suggestionRequest: SuggestionRequest): Task {

    }

    fun deleteSuggestion(suggestId: Long) {

    }

    private companion object {
        const val TAG = "DAOSuggestion"
    }
}