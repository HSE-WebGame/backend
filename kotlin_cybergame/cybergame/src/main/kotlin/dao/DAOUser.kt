package com.hse.cyber.dao

import com.hse.cyber.config.DBConnection
import com.hse.cyber.constants.DBQueriesUser
import com.hse.cyber.model.NoDatabaseConnectionException
import com.hse.cyber.model.User
import com.hse.cyber.utills.Logger
import com.hse.cyber.utills.UserFieldValidator.Companion.validateUserFieldsRegister
import java.sql.Connection
import java.sql.ResultSet
import java.sql.SQLException
import com.hse.cyber.constants.UserFiledName
import com.hse.cyber.model.DuplicateUserException
import com.hse.cyber.model.InvalidFormatException
import com.hse.cyber.model.UnexpectedResultException
import com.hse.cyber.model.UserAuth
import com.hse.cyber.model.UserNotFoundException
import com.hse.cyber.model.UserRegister
import com.hse.cyber.utills.UserFieldValidator.Companion.validateUserFieldsAuth
import org.springframework.stereotype.Component

@Component
class DAOUser {
    private val connection: Connection

    init {
        try {
            connection = DBConnection.getConnection() ?: throw NoDatabaseConnectionException()
        } catch (e: SQLException) {
            Logger.err(e)
            throw NoDatabaseConnectionException()
        }
    }

    fun registerUser(userRegister: UserRegister): Long {
        Logger.log(TAG, "registerUser $userRegister")
        if (!validateUserFieldsRegister(userRegister)) {
            throw InvalidFormatException()
        }
        try {
            connection.prepareStatement(DBQueriesUser.REGISTER_USER).use { statement ->
                statement.setString(1, userRegister.name)
                statement.setString(2, userRegister.login)
                statement.setString(3, userRegister.password)
                statement.setString(4, userRegister.secretWord)
                statement.setBoolean(5, false)
                statement.executeQuery().use { resultSet ->
                    if (resultSet.next()) {
                        return resultSet.getLong(1)
                    } else {
                        throw UnexpectedResultException()
                    }
                }
            }
        } catch (e: SQLException) {
            if (e.sqlState == "23505") {
                throw DuplicateUserException()
            }
            throw e
        }
    }

    fun authenticateUser(userAuth: UserAuth): User {
        Logger.log(TAG, "authenticateUser $userAuth")
        if (!validateUserFieldsAuth(userAuth)) {
            throw InvalidFormatException()
        }
        connection.prepareStatement(DBQueriesUser.AUTHENTICATE_USER).use { statement ->
            statement.setString(1, userAuth.login)
            statement.setString(2, userAuth.password)
            statement.executeQuery().use { resultSet ->
                if (resultSet.next()) {
                    val user = mapUser(resultSet)
                    Logger.log(TAG, "authenticateUser final user: $user")
                    return user
                }
            }
        }
        throw UserNotFoundException()
    }

    fun getUserById(userId: Long): User {
        Logger.log(TAG, "getUserById $userId")
        connection.prepareStatement(DBQueriesUser.GET_BY_ID).use { statement ->
            statement.setLong(1, userId)
            statement.executeQuery().use { resultSet ->
                if (resultSet.next()) {
                    val user = mapUser(resultSet)
                    Logger.log(TAG, "getUserById final user: $user")
                    return user
                }
            }
        }
        throw UserNotFoundException()
    }

    fun getAllUsers(): List<User> {
        Logger.log(TAG, "getAllUsers")
        val users = mutableListOf<User>()
        connection.prepareStatement(DBQueriesUser.GET_ALL_USERS).use { statement ->
            statement.executeQuery().use { resultSet ->
                while (resultSet.next()) {
                    val user = mapUser(resultSet)
                    Logger.log(TAG, "getAllUsers add $user to list")
                    users.add(user)
                }
            }
        }
        if (users.isEmpty()) {
            throw UserNotFoundException()
        }
        Logger.log(TAG, "getAllUsers final list: $users")
        return users
    }

    private fun mapUser(resultSet: ResultSet): User {
        return User(
            userId = resultSet.getLong(UserFiledName.USER_ID),
            name = resultSet.getString(UserFiledName.NAME),
            login = resultSet.getString(UserFiledName.LOGIN),
            secretWord = resultSet.getString(UserFiledName.SECRET_WORD),
            isAdmin = resultSet.getBoolean(UserFiledName.IS_ADMIN),
        )
    }

    private companion object {
        const val TAG = "DAOUser"
    }
}