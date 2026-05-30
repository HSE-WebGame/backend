package com.hse.cyber.dao

import com.hse.cyber.config.DBConnection
import com.hse.cyber.model.NoDatabaseConnection
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
            connection = DBConnection.getConnection() ?: throw NoDatabaseConnection()
        } catch (e: SQLException) {
            Logger.err("$e")
            throw NoDatabaseConnection()
        }
    }

    fun getSuggestionList(): List<Suggestion> {

    }

    fun createSuggestion(suggestionRequest: SuggestionRequest): Task {

    }

    fun deleteSuggestion(suggestId: Long) {

    }
}