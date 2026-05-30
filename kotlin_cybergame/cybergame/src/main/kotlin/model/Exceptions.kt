package com.hse.cyber.model

import java.io.IOException

class InvalidFormat: IOException() {
    override val message: String
        get() = "Invalid data format"
}

class NoDatabaseConnection: IOException() {
    override val message: String
        get() = "No database connection"
}

class UserNotFoundException: IOException() {
    override val message: String
        get() = "User not found"
}