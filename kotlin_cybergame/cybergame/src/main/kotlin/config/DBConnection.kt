package com.hse.cyber.config

import com.hse.cyber.constants.DBParams
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class DBConnection {
    companion object {
        private var connection: Connection? = null

        @Throws(SQLException::class)
        fun getConnection(): Connection? {
            if (connection == null) {
                connection = DriverManager.getConnection(DBParams.URL, DBParams.NAME, DBParams.PASSWORD)
            }
            return connection
        }
    }
}