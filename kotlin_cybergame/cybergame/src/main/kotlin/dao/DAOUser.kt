package com.hse.cyber.dao

import com.hse.cyber.config.DBConnection
import com.hse.cyber.constants.DBQueries
import com.hse.cyber.model.InvalidFormat
import com.hse.cyber.model.NoDatabaseConnection
import com.hse.cyber.model.User
import com.hse.cyber.model.UserNotFoundException
import com.hse.cyber.utills.Logger
import com.hse.cyber.utills.UserFieldValidator.Companion.checkUserFields
import com.hse.cyber.utills.UserFieldValidator.Companion.checkUserFieldsAuth
import java.sql.Connection
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Types

class DAOUser {
    private val connection: Connection

    init {
        try {
            connection = DBConnection.getConnection() ?: throw NoDatabaseConnection()
        } catch (e: SQLException) {
            Logger.err("$e")
            throw NoDatabaseConnection()
        }
    }

    fun registerUser(registerUser: User): User {
        if (!checkUserFields(registerUser)) {
            throw InvalidFormat()
        }
        connection.prepareCall(DBQueries.REGISTER_USER).use { callableStatement ->
            callableStatement.setString(1, registerUser.userLogin)
            callableStatement.setString(2, registerUser.userPassword)
            callableStatement.setString(3, registerUser.userName)
            callableStatement.setString(4, registerUser.secretWord)
            callableStatement.setBoolean(5, registerUser.isAdmin)


            callableStatement.registerOutParameter(6, Types.REF_CURSOR)
            callableStatement.execute()

            val resultSet = callableStatement.getObject(7) as ResultSet

            return if (resultSet.next()) {
                User(
                    id = resultSet.getLong("id"),
                    userName = resultSet.getString("user_name"),
                    userLogin = resultSet.getString("user_login"),
                    userPassword = resultSet.getString("user_password"),
                    secretWord = resultSet.getString("secret_word"),
                    isAdmin = resultSet.getBoolean("is_admin"),
                ).also {
                    resultSet.close()
                }
            } else {
                resultSet.close()
                throw UserNotFoundException()
            }
        }
    }

    fun authenticateUser(userAuth: User): User {
        if (!checkUserFieldsAuth(userAuth)) {
            throw InvalidFormat()
        }

        connection.prepareCall(DBQueries.AUTHENTICATE_USER).use { callableStatement ->
            callableStatement.setString(1, userAuth.userLogin)
            callableStatement.setString(2, userAuth.userPassword)

            callableStatement.registerOutParameter(3, Types.REF_CURSOR)
            callableStatement.execute()

            val resultSet = callableStatement.getObject(3) as ResultSet

            return if (resultSet.next()) {
                User(
                    id = resultSet.getLong("id"),
                    userName = resultSet.getString("user_name"),
                    userLogin = resultSet.getString("user_login"),
                    userPassword = resultSet.getString("user_password"),
                    secretWord = resultSet.getString("secret_word"),
                    isAdmin = resultSet.getBoolean("is_admin"),
                ).also {
                    resultSet.close()
                }
            } else {
                resultSet.close()
                throw UserNotFoundException()
            }
        }
    }
}