package com.hse.cyber.constants

object DBQueriesSuggestion {
    const val SUGGESTION_CRETE = "SELECT suggestion_create(?, ?, ?, ?, ?, ?);"
    const val SUGGESTION_GET = "SELECT * FROM suggestion_get();"
    const val SUGGESTION_DELETE = "CALL suggestion_delete(?);"
}