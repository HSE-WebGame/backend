package com.hse.cyber.utills

class Logger {
    companion object {
        fun log(message: String) {
            println(message)
        }

        fun log(tag: String, message: String) {
            println("$tag $message")
        }

        fun err(t: Throwable) {
            System.err.println(t.toString())
        }
    }
}
