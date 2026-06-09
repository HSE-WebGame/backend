package com.hse.cyber.model

import java.io.IOException
import java.sql.SQLException

class InvalidFormatException: RuntimeException() {
    override val message: String
        get() = "Invalid data format"
}

class NoDatabaseConnectionException: SQLException() {
    override val message: String
        get() = "No database connection"
}

class UserNotFoundException: IOException() {
    override val message: String
        get() = "User not found"
}

class DuplicateUserException: SQLException() {
    override val message: String
        get() = "Several users with a unique login were found"
}

class UnexpectedResultException: RuntimeException() {
    override val message: String
        get() = "The result was unexpected or null"
}

class TaskNotFoundException: RuntimeException() {
    override val message: String
        get() = "Task not found"
}

class SuggestionNotFoundException: RuntimeException() {
    override val message: String
        get() = "Suggestion not found"
}

class WrongTaskAnswerException: RuntimeException() {
    override val message: String
        get() = "Wrong task answer"
}

class TaskAlreadySolvedException: RuntimeException() {
    override val message: String
        get() = "Task already solved"
}