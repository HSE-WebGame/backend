package com.hse.cyber.utills

class Logger {
    companion object {
        fun log(message: String) {
            println(message)
        }

        fun err(message: String) {
            System.err.println(message)
        }
    }
}
