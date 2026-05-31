package com.hse.cyber.constants

object DBQueriesUser {
    const val AUTHENTICATE_USER = "SELECT * FROM user_auth(?, ?)"
    const val REGISTER_USER = "SELECT user_register(?, ?, ?, ?, ?)"
    const val GET_BY_ID = "SELECT * FROM user_get(?)"
    const val GET_ALL_USERS = "SELECT * FROM user_get_all()"
}