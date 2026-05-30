package com.hse.cyber.dao

import com.hse.cyber.config.DBConnection
import com.hse.cyber.constants.DBQueriesUser
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
        connection.prepareCall(DBQueriesUser.REGISTER_USER).use { callableStatement ->
            callableStatement.setString(1, registerUser.login)
            callableStatement.setString(2, registerUser.password)
            callableStatement.setString(3, registerUser.name)
            callableStatement.setString(4, registerUser.secretWord)
            callableStatement.setBoolean(5, registerUser.isAdmin)


            callableStatement.registerOutParameter(6, Types.REF_CURSOR)
            callableStatement.execute()

            val resultSet = callableStatement.getObject(7) as ResultSet

            return if (resultSet.next()) {
                User(
                    id = resultSet.getLong("id"),
                    name = resultSet.getString("user_name"),
                    login = resultSet.getString("user_login"),
                    password = resultSet.getString("user_password"),
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

        connection.prepareCall(DBQueriesUser.AUTHENTICATE_USER).use { callableStatement ->
            callableStatement.setString(1, userAuth.login)
            callableStatement.setString(2, userAuth.password)

            callableStatement.registerOutParameter(3, Types.REF_CURSOR)
            callableStatement.execute()

            val resultSet = callableStatement.getObject(3) as ResultSet

            return if (resultSet.next()) {
                User(
                    id = resultSet.getLong("id"),
                    name = resultSet.getString("user_name"),
                    login = resultSet.getString("user_login"),
                    password = resultSet.getString("user_password"),
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